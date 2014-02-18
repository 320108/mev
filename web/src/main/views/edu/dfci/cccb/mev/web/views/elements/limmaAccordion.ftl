<div class="accordion" id="{{cluster.parentId}}" ng-repeat="limma in previousLimmaAnalysis">
    <div class="accordion-group">
    
	    <div class="accordion-heading">
	      <a class="accordion-toggle" data-toggle="collapse" data-parent="{{limma.dataParent}}" href="{{limma.href}}">
	        {{limma.datar.type}} : {{limma.name}}
	      </a>
	    </div> <!-- End Heading Div -->
	    
	    <div id="{{limma.divId}}" class="accordion-body collapse">
	      <div class="accordion-inner">
	            
	            <div class="row-fluid">
	            <div class="span12>
	            
		            <div class="row-fluid">
		            
		              <div class="pull-left">
		              
		                <button class="btn btn-success" >
			                <a href="/dataset/{{datasetName}}/analysis/{{limma.name}}?format=tsv">
			                  <i class="icon-white icon-download"></i> Download
			                </a> 
			            </button>
		              
		              </div>
			            
			          <div class="pull-right">
			            <form class="form-inline">
						  Thresholds: <input type="text" class="input-small" placeholder="P-Value">
						  <input type="text"  class="input-small" placeholder="Log Fold">
						 
						</form>
					  </div>
			            
			            <div id="limmaResultsNotSignificant" ng-hide="limma.datar.results">
			              <hr>
			              <p>No Results!</p>
			              
			            </div>
			            
			        </div>
		            
		            <div class="row-fluid">
			        	<div class="limma-table" id="limmaResultsTable" ng-hide="!limma.datar.results || !showLimmaTables">
			        	
		                    <table class="table table-striped table-bordered">
		                            <thead>
		                                    <tr>
		                                      <th ng-repeat="header in ['ID', 'Log-Fold-Change', 'Average Expression', 'P-Value', 'Q-Value']">{{header}}</th>
		                                    </tr>
		                            </thead>
		                            <tbody>
		                                    <tr ng-repeat="row in limma.datar.results | orderBy:'-pValue' ">
		                                            <td>
		                                                    {{row["id"]}}
		                                            </td>
		                                            <td>
		                                                    {{row["logFoldChange"]}}
		                                            </td>
		                                            <td>
		                                                    {{row["averageExpression"]}}
		                                            </td>
		                                            <td>
		                                                    {{row["pValue"]}}
		                                            </td>
		                                            <td>
		                                                    {{row["qValue"]}}
		                                            </td>
		                                    </tr>
		                            </tbody>
		                    </table>
		                </div>
		            </div>
                
                </div> <!-- End Span -->
                </div> <!-- End Row -->
	        
	      </div>
	    </div> <!-- End Body Div -->
    
    </div> <!-- End Accordion Grouping -->
</div> <!-- End Accordion Definition -->
