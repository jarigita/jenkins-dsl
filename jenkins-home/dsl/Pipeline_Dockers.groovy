execStored = 10
cron = "00 22 * * 1-5"
name = "Pipeline_Dockers"

pipelineJob(name){
    description('Pipeline excute jobs of dockers')

    logRotator(execStored,execStored,execStored,execStored)

    authorization{
      blocksInheritance(false)
      permission('hudson.model.Item.Read','authenticated')
    }

    triggers{
        cron(cron)
    }

    definition{
        cps{
            sandbox()
            script('''
                def result=" "
                pipeline{
                    agent any
                    stages{
                        stage('Export ddbbs') {
                            parallel {
                                stage('Export Mongo'){
                                    steps{
                                        catchError(buildResult:'FAILURE',stageResult:'FAILURE'){
                                            script{
                                                build(job:"Docker_Export_Mongo")
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        stage('Build images') {
                            parallel {
                                stage('Build Mongo'){
                                    steps{
                                        catchError(buildResult:'FAILURE',stageResult:'FAILURE'){
                                            script{
                                                 build(job:"Docker_Build_Mongo")
                                            }
                                        }
                                    }
                                }
                                stage('Build Docker Images'){
                                    steps{
                                        catchError(buildResult:'FAILURE',stageResult:'FAILURE'){
                                            script{
                                                build(job:"Docker_Build_Docker_Images")
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        stage('Clean and update') {
                            parallel {
                                stage('Delete images'){
                                    steps{
                                        catchError(buildResult:'FAILURE',stageResult:'FAILURE'){
                                            script{
                                                build(job:"Docker_Delete_Images")
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            '''.stripIndent())
        }
    }
}
