# CrabScore-Refactory

I hope to take this opportunity of refactoring an old project to complete my graduation thesis (design) and consolidate the use of *Mybatis Plus* and other components.

## New Features & Bug Fixes

It's a third main version of CrabScore. Relative to the previous version:

* I use Mybatis Plus to replace generated mybatis dynamic SQL in XML file
* use optimistic lock to guarantee concurrency conflict during data updating
* use `@Async` to speed ​​up some processes and many `TODO` or `FIXME` feature.
* To simplify object storage, I perform this feature inside the service instead of using third-party services. For more information, please check `top.spencercjh.crabscore.refactory.service.BaseUploadFileService` . These codes reference some businesses codes I wrote during my internship.

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

## Conventions

* Test

There is only service unit test in code form. I perform api test by *Swagger UI* .

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
  │  │  └─top.spencercjh.crabscore.refactory
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
      │  └─top.spencercjh.crabscore.refactory
      └─resources
```
