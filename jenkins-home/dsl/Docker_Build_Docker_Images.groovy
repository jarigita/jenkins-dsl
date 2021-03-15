execStored = 10
name = "Docker_Build_Docker_Images"
script = readFileFromWorkspace('../../' + name +'.script')

job(name) {

  description('This job builds all Smart M2M platform docker images')
  
  authorization {
    blocksInheritance(false)
    permission('hudson.model.Item.Read', 'authenticated')
    permission('hudson.model.Item.Workspace', 'authenticated')
  }

  customWorkspace('/var/lib/jenkins/shared_workspace_all_jobs')

  parameters {
    stringParam('mc_core', 'develop', 'Repo branch to be used, must exists at repo')
    stringParam('mc_portal', 'develop', 'Repo branch to be used, must exists at repo')
    stringParam('mc_pybe', 'develop', 'Repo branch to be used, must exists at repo')
    stringParam('microgw', 'develop', 'Repo branch to be used, must exists at repo')
    stringParam('mc_be', 'develop', 'Repo branch to be used, must exists at repo')
    stringParam('mc_userdoc', 'develop', 'Repo branch to be used, must exists at repo')
    stringParam('m2m_nitro_components', 'develop', 'Repo branch to be used, must exists at repo')
    stringParam('tagName', 'latest', 'This will be used as docker images tag, not allowed numbers and alphanumerics')
  }

  logRotator(-1,execStored,-1,-1)
  
  wrappers {
    timestamps()
    colorizeOutput()
    credentialsBinding {
      usernamePassword('SECRET_USER', 'SECRET_PASS', 'contint')
    }
  }

  steps {
    shell(script)
  }

  publishers {
    extendedEmail {
      recipientList('eloy.monterodelrio@telefonica.com,brian.romero@ust-global.com')
      triggers {
        success {
          subject('SUCCESS-dsl ' + name)
          sendTo {
            recipientList()
          }
        }
        failure {
          subject('ERROR-dsl ' + name)
          sendTo {
            recipientList()
          }
        }

      }
    }
  }
}


