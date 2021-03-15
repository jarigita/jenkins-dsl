execStored = 10
name = "Docker_Export_Mongo"
script = readFileFromWorkspace('../../' + name +'.script')

job(name) {

  description('Export dump Mongo database.')

  logRotator(-1,execStored,-1,-1)

  authorization {
    blocksInheritance(false)
    permission('hudson.model.Item.Workspace', 'authenticated')
    permission('hudson.model.Item.Read', 'authenticated')
  }

  wrappers {
    timestamps()
    colorizeOutput()
    credentialsBinding {
      usernamePassword('SECRET_USER_2', 'SECRET_PASS_2', 'sysadminm2m')
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
          subject('SUCCESS-dsl ${ENV, var="USER_NAME"} ' + name)
          sendTo {
            recipientList()
          }
        }
        failure {
          subject('ERROR-dsl ${ENV, var="USER_NAME"} ' + name)
          sendTo {
            recipientList()
          }
        }
      }
    }
  }
}
