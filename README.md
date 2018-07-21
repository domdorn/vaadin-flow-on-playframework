# play 2.6 with Vaadin 10 - PROOF OF CONCEPT - WORK IN PROGRESS
This is work in progress.. at the moment, a proof of concept - its far from production usage.


In this project I (Dominik Dorn @domdorn) am trying to get Vaadin 10 "FLOW" working on PlayFramework 2.6

It is based on modified code from 
https://github.com/henrikerola/play-vaadin-integration
 which is in the app/org/vaadin/playintegration folder.

The example we're trying to get running is the "beverage buddy" application, that you can download from vaadin at
https://vaadin.com/start/v10-simple-ui 

What is working? 
----
You can start the app and go to 
http://localhost:9000

this will redirect you to the configured UI path
http://localhost:9000/my-vaadin-app/

There you'll get a list of reviews, filtering them already works

You can also load the categories at
http://localhost:9000/my-vaadin-app/categories

and filter through them. 

TODOs
---
 * High Prio
   * none at the moment
   * make push work using actors 
   * correctly set contextRoot in the various places (marked with TODO)
   * allow / contextRoot
   * allow multiple context-Roots / UIs with different addresses
 * Medium Prio
   * Asset handling code in VaadinPlayService / VaadinController needs cleanup -> extract code to VaadinResources and cleanup
   * Asset handling code needs to send proper caching headers
   * appUrl in org.vaadin.playintegration.VaadinPlayService.getMainDivId is hardcoded.. get this from the request like in VaadinServletService
     * figure out if this is actually needed.. doesn't look like
   * org.vaadin.playintegration.VaadinPlayService.isOtherRequest is hardcoded atm
   * org.vaadin.playintegration.VaadinPlayService.requestCanCreateSession is hardcoded atm
   * Routes are registered manually atm .. see org.vaadin.playintegration.VaadinPlayService.createRouteRegistry 
 * Low Prio
   * Asset handling code needs a cache, so we don't have to do classpath lookups all the time.. take a look at Play's Assets for inspiration
   * Vaadins FavIconHandler is hardcoded to Servlet Environments, bug: [https://github.com/vaadin/flow/issues/4426]
     * a workaround has been created with Plays' routes-mechanism
   * Handle Vaadin-Sessions using Actors.. maybe take some inspiration from 
     https://github.com/otto-ringhofer/vaactor  
  
How to start it?
----

`sbt run`

`sbt -jvm-debug 5005 run `

