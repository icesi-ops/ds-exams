# "Singara": Docker and Kubernetes web app not working

## Description

Apply The Kubernetes YAML manifests in this repository. The objective is to access from the host the "webapp" web server deployed and find what message it serves (it's a name of a town or city btw). In order to pass the check, the webapp Docker container should not be run separately outside Kubernetes as a shortcut.

## Test

<kbd>curl localhost:8888</kbd> returns a value from the webapp deployed Kubernetes pod.

<b>check.sh</b>

```
#!/usr/bin/bash
key=$(curl -s localhost:8888)
key=$(echo $key|tr -d '\r')
res=$(echo -n $key|md5sum|awk '{print $1}')
res=$(echo $res|tr -d '\r')
res2=$(sudo docker ps -a|grep 8888)

if [[ "$res" = "c6faeb6a2f39140cccf1f69cc3be84cd" && "$res2" = "" ]]
then
  echo -n "OK"
else
  echo -n "NO"
fi
```

## Clues

<b>1. </b>Review K8s and pod state for our namespace: <kbd>kubectl get all -n web</kbd> , <kbd>kubectl describe pod -n web</kbd><br><br>
<b>2. </b>The status of the webapp-deployment is <i>ImagePullBackOff</i>. The webapp pod description says: <i>Failed to pull image "webapp"</i>. Resolve it
<b>4. </b>There is a <i>registry</i> image in Dockerhub you can use. <br><br>
<b>5. <kbd>docker run -d -p 5000:5000  registry</kbd> ; <kbd>docker tag webapp localhost:5000/webapp</kbd> ; <kbd>docker push localhost:5000/webapp</kbd>. 
<b> <kbd>kubectl port-forward deployments/webapp-deployment 8888 -n web &</kbd> ; <kbd>curl localhost:8888</kbd> (another option to get the message from the webapp is to <i>kubectl exec</i> into the pod and curl locally but this is not verified by the "Check Solution" script).