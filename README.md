[![Identity Service](https://github.com/trackerforce/trackerforce-services/actions/workflows/tf-identity.yml/badge.svg)](https://github.com/trackerforce/trackerforce-services/actions/workflows/tf-identity.yml)
[![Management Service](https://github.com/trackerforce/trackerforce-services/actions/workflows/tf-management.yml/badge.svg)](https://github.com/trackerforce/trackerforce-services/actions/workflows/tf-management.yml)
[![Session Service](https://github.com/trackerforce/trackerforce-services/actions/workflows/tf-session.yml/badge.svg)](https://github.com/trackerforce/trackerforce-services/actions/workflows/tf-session.yml)

![Trackerforce: Cloud-based follow up application](https://github.com/petruki/trackerforce-assets/blob/master/logo/trackerforce_grey.png)

# About  

**Trackerforce Services** is a Spring multi-module project that supports the core functionalities available through the Trackerforce Management, which includes:
- Identity Management
- Business Configuration
- Session Executor

## Architecture overview

Trackerforce Services was built to integrate main core services into a distributed ecosystem. Although core services are maintained under the same multi-module project, they are deployed separately as well as maintained independently, means that changes can trigger one or more module of this project.

Two categories of Services were created, Global (Identity) and Multi-tenant (Management and Session).

As for the core libraries, the two common libraries follows the dependency structure below for the Multi-tenant Services:
> Service Module -> Common Multi-tenancy Library -> Common Library

For the Global Service:
> Service Module -> Common Library

![Trackerforce: Macro Architecture](https://github.com/trackerforce/trackerforce-assets/blob/master/documentation/macro_architecture_v2.png)

# Running

**Requirements**
- MongoDB
- Java 11
- Maven 3.8

Run the services indicated by the module name:
```
mvn spring-boot:run -pl trackerforce-identity
mvn spring-boot:run -pl trackerforce-management
mvn spring-boot:run -pl trackerforce-session
```

* * *

## Donations
Donations for coffee, cookies or pizza are extremely welcomed.</br>
Please, find the sponsor button at the top for more options.

[![paypal](https://www.paypalobjects.com/en_US/i/btn/btn_donateCC_LG.gif)](https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=9FKW64V67RKXW&source=url)