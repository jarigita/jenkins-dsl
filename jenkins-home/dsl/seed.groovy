String [] viewNames = [ "Dockers_Ci", "Vulnerability_Checks", "Pipelines" ]

String[][] viewJobMatrix = [ [ "Docker_Build_Docker_Images", "Docker_Build_Mongo", "Docker_Build_Mongo_Sharded", "Docker_Build_Oracle_Remote", "Docker_Delete_Images", "Docker_Export_Mongo", "Docker_Export_Oracle", "Docker_Update_Oracle_Image" ], [ "Vulnerability_check_mc_core_depts", "Vulnerability_check_mc_portal_depts", "Vulnerability_check_mc_phpbe_depts", "Vulnerability_check_mc_pybe_depts", "Vulnerability_check_mc_tdr_depts", "Vulnerability_check_mc_hlr_proxy_depts", "Vulnerability_check_mc_ugw_adapter_depts", "Vulnerability_check_mc_tdaf_api_thor_depts", "Vulnerability_check_mc_tdaf_api_acdc_depts", "Vulnerability_check_mirror_servers", "Vulnerability_check_mirror_Openscap" ], [ "Pipeline_Dockers", "Pipeline_Vulnerabilities" ] ]


for (view = 0; view < viewNames.length; view++) {
  //Create views
  listView(viewNames[view]) {
    //Asociate jobs to view
    for (job = 0; job < viewJobMatrix[view].length; job++) {
      println "Adding to view: " + viewNames[view] + "job: " + viewJobMatrix[view][job] 	
      jobs {
        name(viewJobMatrix[view][job])
      }
    }
    columns {
      status()
      weather()
      name()
      lastDuration()
    }
  }   
}

