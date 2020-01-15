import should.*

def call(body) {

    if (should.test(env.BRANCH_NAME) == true){

        parallel(
          "LintCheck": {
            sh script: "yarn lint", label: "Lint Check"
          },
          "FormatCheck": {
            sh script: "yarn format:check", label: "Format Check"
          },
          "UnitTests": {
            sh script: "yarn test", label: "Unit Tests"
          },
          "CoverageThreshold": {
            sh script: "yarn test:cov", label: "Cov Check"
          },
          "E2eTests": {
            sh script: "yarn test:e2e", label: "E2E Tests"
          }
          )

    }
}

