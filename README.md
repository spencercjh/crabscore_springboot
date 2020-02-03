# *CrabScore*-Refactory

It's a third main version of *CrabScore*.

The last time I push on *CrabScore* was over a year ago (November 2018).

*CrabScore* is a simple content management system(CMS) based on Android. I can't write frond-end code at all, so this is a very immature *software* :D. Now I pick it up again and give up Android, hoping to refactory back-end to make it matching my present technical capabilities.

I hope to take this opportunity of refactoring an old project to complete my graduation thesis (design) and consolidate the use of *Mybatis Plus* and other components.

## New Features & Bug Fixes

Relative to the previous version:

* Use *Mybatis Plus* to replace generated mybatis dynamic SQL in XML file and implement CRUD **as fast as possible**;
* Use **optimistic lock** (`@Version` annotation in *Mybatis Plus* ) to guarantee concurrency conflict during data updating;
* Use `@Async` to speed ​​up some processes and many `TODO` or `FIXME` feature by **asynchronous operation**;
* To simplify **object storage**, I perform this feature inside the service instead of using third-party services(such as OBS). For more information, please check `top.spencercjh.CrabScore.refactory.service.BaseUploadFileService` . These codes reference some businesses codes I wrote during my internship;
* Constrain NPEs using `org.jetbrains.annotations`. 
* Implement **RESTful** Api **more correctly** than before.
* Use *Spring Security* to implement authentication and authorization.

## To Do in Feature

* Convert this **refactoried** monolithic service to *Spring Cloud* project like before and make it better.

## Components

Detail Dependencies Information in `pom.xml` 

* *Spring Boot* 2.2.4.RELEASE (inline with Spring Core 5.2.3.RELEASE)
* Spring Boot Starter *Actuator*
* Spring Boot Starter Data *Redis*
* Spring Boot Starter *Web*
* Spring Boot Starter *Mail*
* *Mybatis Plus* Boot Starter 3.3.1 (inline with *Mybatis* 2.0.3)
* *Springfox Swagger* 2.9.2
* *Junit Jupiter* 5.5.2
* Spring Boot Starter *Security*
* *Spring Security Oauth2*

## Conventions

* org.jetbrains.annotations

Use it to constrain NPEs.

* Test

All codes need to be tested!

* RESTful Api, reference

1. [ruanyf@restful-api](http://www.ruanyifeng.com/blog/2014/05/restful_api.html)
2. [ruanyf@restful-api-best-practices](http://www.ruanyifeng.com/blog/2018/10/restful-api-best-practices.html)

* Project structure

```sh

├─log # Default log directory
├─SQL # DB DDL
├─orign # Original demand
└─src
  ├─main
  │  ├─java
  │  │  └─top.spencercjh.crabScore.refactory
  │  │   ├─config
  │  │   ├─controller # controller
  │  │   ├─mapper # dao
  │  │   ├─model # model
  │  │   │  ├─dto
  │  │   │  ├─enums
  │  │   │  └─vo
  │  │   ├─service # service
  │  │   │  └─impl
  │  │   └─util # tool
  │  └─resources
  │      ├─mapper # XML
  │      ├─static
  │      └─templates
  └─test
      ├─java
      │  └─top.spencercjh.crabScore.refactory
      └─resources
```
