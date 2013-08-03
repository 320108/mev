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
package edu.dfci.cccb.mev.domain;

import static org.springframework.context.annotation.ScopedProxyMode.TARGET_CLASS;
import static org.springframework.web.context.WebApplicationContext.SCOPE_SESSION;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author levk
 * 
 */
@Component
@Scope (value = SCOPE_SESSION, proxyMode = TARGET_CLASS)
public class Heatmaps {

  private final Map<String, Heatmap> heatmaps = new HashMap<> ();

  public Collection<String> list () {
    return heatmaps.keySet ();
  }

  public Heatmap get (String id) throws HeatmapNotFoundException {
    Heatmap result = heatmaps.get (id);
    if (result == null)
      throw new HeatmapNotFoundException (id);
    return result;
  }
  
  public boolean contains (String id) {
    return heatmaps.containsKey (id);
  }

  public void delete (String id) throws HeatmapNotFoundException {
    if (heatmaps.remove (id) == null)
      throw new HeatmapNotFoundException (id);
  }
  
  public void put (String id, Heatmap heatmap) {
    heatmaps.put (id, heatmap);
  }
}
