### PROYECTO FINAL

## BASE DE DATOS REDIS

Por defecto todos los elementos de Kubernetes se crean en el name space por defecto.
Por lo que se va a crear un nuevo name space para realizar una buena gestion de los 
elemento.

El comando para la creacion del namespace es:
``` kubectl create ns redis ```
El comando para verificar que se creo correctamente es:
``` kubectl get ns ```

## CRECION DE LOS ARCHIVOS PARA LA DB

### Definir clase de almacenamiento

Los pods en el cluster de kubernetes no lamacenan datos de forma permanentes.
Por lo que se necesita de un volumen para tener persistencia.

El archivo ``` sc.yaml ``` contiene la definicion para una clase de almacenamiento
para el uso del volumen.

El comando para ejecutar el manifiesto es :
``` kubectl apply -f sc.yaml ```

### Definir volumen de persistencia

En el archivo ``` pv.yaml ``` esta la definicion para crear un cluster de redis con
3 pods, un mastro y dos esclavos.

El comando para ejecutar el manifiesto es :
``` kubectl apply -f pv.yaml ```

### Definir un ConfigMap

El configMap en el cluster de Kubernetes es un almacen de clave valor, que se puede
usar para la configuracion de redis en el cluster.

En el archivo ``` redis-config.yaml ``` se encuentra la definicion del configmap
sacado de ``` https://gist.github.com/bharathirajatut/dcebde585eba5ac8b1398b8ed653d32d ```

El comando para ejecutar el manifiesto es :
``` kubectl apply -n redis -f redis-config.yaml ```

### Despliegue del StatefulSet

El StatefulSet ofrece nombres de pods ordenados empezando por cero y recrea el pod con el mismo nombre cada vez que el pod muere o falla. Un pod puede fallar en cualquier momento.

El archivo ``` redis-statefulset.yaml ``` contiene la definicion del statefulset.

El comando para ejecutar el manifiesto es :
``` kubectl apply -n redis -f redis-statefulset.yaml ```

### Creacion del servicio

No se puede acceder directamente a la aplicacion que se ejecuta en el pod.
Por lo que para poder acceder se necesita de un servicio de kubernetes.

En el archivo ``` redis-service.yaml ``` se encuentra la definicion del servicio.

El comando para ejecutar el manifiesto es :
``` kubectl apply -n redis -f redis-service.yaml ```

