/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.dfci.cccb.mev.dataset.domain.metamodel;

import static edu.dfci.cccb.mev.dataset.domain.contract.Dimension.Type.COLUMN;
import static edu.dfci.cccb.mev.dataset.domain.contract.Dimension.Type.ROW;
import static edu.dfci.cccb.mev.dataset.domain.prototype.AbstractDataSourceValues.COLUMN_FIELD_NAME;
import static edu.dfci.cccb.mev.dataset.domain.prototype.AbstractDataSourceValues.ROW_FIELD_NAME;
import static edu.dfci.cccb.mev.dataset.domain.prototype.AbstractDataSourceValues.VALUE_FIELD_NAME;
import static java.util.UUID.randomUUID;
import static org.eobjects.metamodel.schema.ColumnType.DOUBLE;
import static org.eobjects.metamodel.schema.ColumnType.VARCHAR;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

import lombok.Getter;
import lombok.Setter;
import lombok.Synchronized;
import lombok.ToString;
import lombok.extern.log4j.Log4j;

import org.eobjects.metamodel.create.CreateTable;
import org.eobjects.metamodel.data.DataSet;
import org.eobjects.metamodel.insert.InsertInto;
import org.eobjects.metamodel.jdbc.JdbcDataContext;
import org.eobjects.metamodel.schema.Table;

import edu.dfci.cccb.mev.dataset.domain.contract.ValueStoreBuilder;
import edu.dfci.cccb.mev.dataset.domain.contract.ValueStoreException;
import edu.dfci.cccb.mev.dataset.domain.contract.Values;
import edu.dfci.cccb.mev.dataset.domain.prototype.AbstractValueStoreBuilder;

/**
 * @author levk
 * 
 */
@ToString
@Log4j
public class MetamodelBackedValueStoreBuilder extends AbstractValueStoreBuilder {

  private @Getter @Setter @Inject @Named("mev-datasource")DataSource dataSource;

  private JdbcDataContext context;
  private Table table;
  private MetamodelBackedValues store;

  @PostConstruct
  private void initialize () throws Exception {
    context = new JdbcDataContext (dataSource);
    String tableName = randomUUID ().toString ();
    CreateTable creator = new CreateTable (context.getDefaultSchema (), tableName);
    creator.withColumn (ROW_FIELD_NAME).ofType (VARCHAR).asPrimaryKey ();
    creator.withColumn (COLUMN_FIELD_NAME).ofType (VARCHAR).asPrimaryKey ();
    creator.withColumn (VALUE_FIELD_NAME).ofType (DOUBLE);
    context.executeUpdate (creator);
    table = context.getDefaultSchema ().getTableByName (tableName);
    log.debug ("Created table " + table);
    store = new MetamodelBackedValues (table, context);
  }

  /* (non-Javadoc)
   * @see
   * edu.dfci.cccb.mev.dataset.domain.contract.ValueStoreBuilder#add(double,
   * java.lang.String, java.lang.String) */
  @Override
  public ValueStoreBuilder add (final double value, final String row, final String column) throws ValueStoreException {
    try (DataSet data = store.query (row, column)) {
      if (data.next ())
        throw new ValueStoreException ().projection (ROW, row).projection (COLUMN, column);
    }
    context.executeUpdate (new InsertInto (table).value (ROW_FIELD_NAME, row)
                                                 .value (COLUMN_FIELD_NAME, column)
                                                 .value (VALUE_FIELD_NAME, value));
    return this;
  }

  /* (non-Javadoc)
   * @see edu.dfci.cccb.mev.dataset.domain.contract.ValueStoreBuilder#build() */
  @Override
  @Synchronized
  public Values build () {
    return store;
  }

  @Override
  public Values build (Map<String,Integer> row, Map<String, Integer> columns) {
    //TODO:fix - this is needed by the FlatFileValueStoreBuilder, 
    //Here, we just do the same as the build() method - need to find a nice way of doing this 
    return build();
  }
}
