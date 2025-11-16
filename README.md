***

<div align="center">
<b>Trackerforce Services</b><br>
</div>

<div align="center">

[![Identity Service](https://github.com/trackerforce/trackerforce-services/actions/workflows/tf-identity.yml/badge.svg)](https://github.com/trackerforce/trackerforce-services/actions/workflows/tf-identity.yml)
[![Management Service](https://github.com/trackerforce/trackerforce-services/actions/workflows/tf-management.yml/badge.svg)](https://github.com/trackerforce/trackerforce-services/actions/workflows/tf-management.yml)
[![Session Service](https://github.com/trackerforce/trackerforce-services/actions/workflows/tf-session.yml/badge.svg)](https://github.com/trackerforce/trackerforce-services/actions/workflows/tf-session.yml)
[![Project status](https://img.shields.io/badge/Project%20status-Idle-orange.svg)](https://img.shields.io/badge/Project%20status-Idle-orange.svg)

</div>

***

![Trackerforce: Cloud-based follow up application](https://github.com/petruki/trackerforce-assets/blob/master/logo/trackerforce_grey.png)

# About  

**Trackerforce Services** is a Spring multi-module project that supports the core functionalities available through the Trackerforce Management, which includes:
- Identity Management
- Business Configuration
- Session Executor

## Architecture overview

Trackerforce Services was built to integrate main core services into a distributed ecosystem. Although core services are maintained under the same multi-module project, they are deployed independently, meaning that changes can trigger one or more module CI pipeline.

Two categories of Services were created, Global (Identity) and Multi-tenant (Management and Session).

As for the core libraries, the two common libraries follows the dependency structure below for the Multi-tenant Services:
> Service Module -> Common Multi-tenancy Library -> Common Library

For the Global Service:
> Service Module -> Common Library

![Trackerforce: Macro Architecture](https://github.com/trackerforce/trackerforce-assets/blob/master/documentation/macro_architecture_v2.png)

# Running

**Requirements**
- MongoDB
- Java 25
- Maven 3.11

Run the services indicated by the module name:
```
mvn spring-boot:run -pl trackerforce-identity
mvn spring-boot:run -pl trackerforce-management
mvn spring-boot:run -pl trackerforce-session
```