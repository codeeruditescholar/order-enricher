# order-enricher


```sql
CREATE DATABASE orderdb;
CREATE ROLE orderdbuser  WITH LOGIN PASSWORD 'orderdbpassword';
ALTER DATABASE orderdb OWNER TO orderdbuser;
GRANT ALL PRIVILEGES ON DATABASE orderdb TO orderdbuser;
```