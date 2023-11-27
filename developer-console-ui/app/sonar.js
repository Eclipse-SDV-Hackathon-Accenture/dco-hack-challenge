const scanner = require('sonarqube-scanner')

scanner(
  {
    serverUrl: process.env.SONAR_URL,
    token: process.env.SONAR_TOKEN,
    options: {
      'sonar.projectKey': 'developer-console-ui',
      'sonar.projectName': 'developer-console-ui',
      'sonar.projectVersion': process.env.APP_VERSION,
      'sonar.sources': '.',
      'sonar.exclusions': '**/__tests__/**/*, **/components/auth/ForceSession.tsx, **/pages/api/**,**/pages/shared/ServerSafePortal.tsx, **/pages/hypercube/**, **/next.config.js, **/jest.config.js, **/sonar.js, **/libs/config.ts, **/assets/constants/constants.js, **/libs/oauth2.ts, **/pages/_app.tsx, **/libs/apollo.ts, **/pages/hypercube/componentNameFinderDropdown.tsx',
      'sonar.coverage.exclusion': '**/__tests__/**/*, **/components/auth/ForceSession.tsx, **/pages/api/**, **/pages/hypercube/**, **/next.config.js, **/jest.config.js, **/sonar.js, **/libs/config.ts, **/assets/constants/constants.js, **/libs/oauth2.ts, **/pages/_app.tsx, **/libs/apollo.ts, **/pages/hypercube/componentNameFinderDropdown.tsx,**/pages/shared/ServerSafePortal.tsx',
      'sonar.test.exclusion': '**/__tests__/**/*, **/components/auth/ForceSession.tsx, **/pages/api/**, **/pages/hypercube/**, **/next.config.js, **/jest.config.js, **/sonar.js, **/libs/config.ts, **/assets/constants/constants.js, **/libs/oauth2.ts, **/pages/_app.tsx, **/libs/apollo.ts, **/pages/hypercube/componentNameFinderDropdown.tsx,**/pages/shared/ServerSafePortal.tsx'
    },
  },
  () => process.exit()
)
