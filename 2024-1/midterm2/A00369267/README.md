## Ejercicio 5
Desplegar una aplicación simple hecha en Golang, la cual se despliegue a través de un recurso de tipo Deployment y gestione 5 replicas de esa aplicación. Además, la aplicación debería de tener un servicio de tipo ClusterIP para exponer la app.
Adicional a eso, crear un healtcheck de tipo livenessprobe para la aplicación. El healtcheck es libre.


use helm para el despliegue de la app

Cada ejercicio debe de ser desplegado en un namespace que contenga el nombre del estudiante. Adicionalmente, para validar que la app funciona se le va a solicitar al estudiante crear un POD de ubuntu el cual haga request a la aplicación a través del DNS que proporciona el servicio asociado a esa app. 