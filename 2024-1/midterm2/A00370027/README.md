# Scenario: Helm Chart Issue in Kubernetes

## Level: Medium

Description: A DevOps engineer created a Helm Chart web-chart with a custom nginx site, however he still gets the default nginx index.html.

You can check for example with minikube service web-chart-service

In addition he should set replicas to 3.

The chart is not working as expected. Fix the configurations so you get the custom HTML page from any nginx pod.

Test: Doing curl on the default port (:80) of any nginx pod returns a Welcome Distribuidos Server page. To check your solution ask to teacher

Time to Solve: 30 minutes.

OS: Debian 11
