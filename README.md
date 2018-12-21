## Running the project

To run the project, execute the following commands from the root directory:

1. `mvn package`

This will compile the Java code, producing a file called `amazing-co-1.0-SNAPSHOT.jar`, which contains our HTTP service.

2. `docker-compose up` 

This will create two images, one running the jar from the previous step and the other running an instance of PostgreSQL database. The service can be accessed through port 25555 and the database through port 5432.

## Database contents

The docker file for the database (`postgres-dockerfile`) contains instructions to copy a python script:

`COPY generate-org.py /db`

This script is then run to generate a csv file with initial data for the database:

`RUN python /db/generate-org.py 100000 5 > /db/data/db.csv`

The csv file is then used by the `createdb.sql` script to add data to a newly created database. In this particular example, python script creates 100K nodes with 5 children each. The id of each node is a UUID.

## API

The HTTP service exposes two endpoints:

`GET /v1/organization/{nodeId}/descendants` - Return all the descendants of node with id equal to *nodeId*. Uses JSON format.

Example usage:

`curl http://localhost:25555/v1/organization/a33fec5e44d843529fea7bbb4f57bede/descendants`

`POST /v1/organization/{nodeId}/parent/{parentId}` - Change the parent of node with id equal to *nodeId*. The id of the new parent is *parentId*.

Example usage:

`curl -X POST http://localhost:25555/v1/organization/a33fec5e44d843529fea7bbb4f57bede/parent/db3e4893e93f4035a4b0d26b7442f91a`

## Architecture
## Tests

## Security considerations
## Performance considerations
## Potential improvements
