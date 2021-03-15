execStored = 10
name = "Docker_Delete_Images"
script = readFileFromWorkspace('../../' + name +'.script')

job(name) {

  description('Delete all images on Jenkins machine')

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
