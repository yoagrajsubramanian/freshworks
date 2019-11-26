# Freshworks DataStore


  DataStore a key-value store provider where JSONObject are stored against
  a String.
  <p>
  DataStore if the bean is created with out filepath it takes the default file location
  DataStoreConstant#DEFAULT_DIR and file name DataStoreConstant#DEFAULT_FILE
  <p>
  To create DataStore bean with file path follow up the example below
  
  ```
  @Bean
  DataStore dataStore() throws IOException {
  return new DataStore("home/yogaraj/fw/datastore/soruce.txt");

  ```
  
  <p>
  If not use DataStore#updateDataStorePath(String) to update source path at any given instance
  </p>
 
  <p>
  To apply TTL to object use the key TTL in your payload. if the key is not matched TTL will not be validated
  </p>

  
