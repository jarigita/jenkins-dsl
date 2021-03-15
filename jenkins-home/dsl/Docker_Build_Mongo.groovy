execStored = 10
name = "Docker_Build_Mongo"
script = readFileFromWorkspace('../../' + name +'.script')

job(name) {

  description('This job builds mongo image every night')

  logRotator(-1,execStored,-1,-1)

  wrappers {
    timestamps()
    colorizeOutput()
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

