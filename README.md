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

The csv file is used by the `createdb.sql` script to add data to a newly created database. In this particular example, python script creates 100K nodes with 5 children each. The id of each node is a UUID.

The format of the only table in the database is as follows:

id | parent | root | height
-- | ------ | ---- | ------
372ae6de-0d0f-499f-a3be-ef916f37c7a2 | 00000000-0000-0000-0000-000000000000 | 372ae6de-0d0f-499f-a3be-ef916f37c7a2 | 0
11a913cd-48ef-4e4a-bd98-27ac72825d8c | 372ae6de-0d0f-499f-a3be-ef916f37c7a2 | 372ae6de-0d0f-499f-a3be-ef916f37c7a2 | 1
a1884142-28b5-4308-81e7-737b630b8b81 | 372ae6de-0d0f-499f-a3be-ef916f37c7a2 | 372ae6de-0d0f-499f-a3be-ef916f37c7a2 | 1

## API

The HTTP service exposes two endpoints:

`GET /v1/organization/{nodeId}/descendants` - Return all the descendants of node with id equal to *nodeId*. Uses JSON format.

Example usage:

`curl http://localhost:25555/v1/organization/a33fec5e44d843529fea7bbb4f57bede/descendants`

`POST /v1/organization/{nodeId}/parent/{parentId}` - Change the parent of node with id equal to *nodeId*. The id of the new parent is *parentId*.

Example usage:

`curl -X POST http://localhost:25555/v1/organization/a33fec5e44d843529fea7bbb4f57bede/parent/db3e4893e93f4035a4b0d26b7442f91a`

## Architecture

The project is implemented using Spring Boot. These are the most important classes:

* *Node* - Model class representing a node in the tree structure.
* *ServiceController* - Controller exposing HTTP endpoints.
* *OrganizationService* - Main business logic. Maintains an in-memory tree of nodes, which is populated from the database when the application starts.
* *SQLRepository* - Talks directly to a database instance to persist any changes.

## Tests

The project does not contain a lot of tests, but several examples of unit tests can be found 
[here](src/test/java/com/kuptel/Organization/OrganizationServiceTest.java).

## Security considerations

Database username and password are hardcoded in [the code](src/main/java/com/kuptel/Organization/Repository/SQLRepository.java). This is obviosuly a security issue. In a production environment, any sensible information of this kind could be provided by a configuration service.

## Memory/Performance considerations

* Application reads the whole tree structure from a database on startup. This could be a bottleneck if the size of the data is really large.
* The whole tree structure is stored in memory to enable quick reads ([here](src/main/java/com/kuptel/Organization/Service/OrganizationService.java)). This could also cause problems if the data is large.
* The tree structure in memory is a shared resource between the read operation (getting descendants of a node) and the write operation (changing the parent of a node). To avoid race conditions a read-write lock is used, which will affect the overall performance.
* The complexity of getting all the descendants of node `A` is O(n), where n is the number of descendant of `A`.
* The complexity of changing the parent of node `A` to node `B` is also O(n), where n is the number of descendants of node `A`. This comes from the fact that we have to update the height of every descendant.

## Potential improvements

* At the moment, when updating the parent of a node, the in-memory tree structure is updated before the database. In case the database update fails, the result will be a mismatch between the two representations. A better solution would be to perform the database update first and then change the in-memory structure. 
