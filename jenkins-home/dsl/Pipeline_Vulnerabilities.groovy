execStored = 10
cron = "00 18 * * 6"
name = "Pipeline_Vulnerabilities"

pipelineJob(name) {
    description('Pipeline of vulnerability jobs')

    logRotator(execStored,execStored,execStored,execStored)

    authorization {
      permission('hudson.model.Item.Read', 'authenticated')
    }

    triggers {
        cron(cron)
    }

    definition {
        cps {
            sandbox()
            script('''
                def result=""
                pipeline{
                    agent any
                    stages{
                        stage('Vulnerability_check_mirror_servers'){
                            steps{
                                script{
                                    build(job:"Vulnerability_check_mirror_servers")
                                }
                            }
                        }
                    }
                }
            '''.stripIndent())
        }
    }
}
