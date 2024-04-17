## Ejercicio 3
Desplegar una aplicación simple hecha en NodeJS, la cual se despliegue a través de un recurso de tipo ReplicationController y gestione 6 replicas de esa aplicación. Además, la aplicación debería de tener un servicio de tipo LoadBalancer para exponer la app.
Adicional a eso, crear un healtcheck de tipo Readinessprobe para la aplicación. El healtcheck es libre. 

use helm para el despliegue de la app, todos valores deben ser parametizables