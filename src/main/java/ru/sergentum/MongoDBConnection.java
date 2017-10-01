package ru.sergentum;

import com.mongodb.*;

import java.util.Iterator;
import java.util.Set;

public class MongoDBConnection {
    MongoClient mongoClient;
    private int Counter;

    private static MongoDBConnection ourInstance = new MongoDBConnection();

    public static MongoDBConnection getInstance() {
        return ourInstance;
    }

    private MongoDBConnection() {

        MongoClientURI uri = new MongoClientURI(
                "mongodb://admin:QWE123asd@cluster0-shard-00-00-q1k7y.mongodb.net:27017,cluster0-shard-00-01-q1k7y.mongodb.net:27017,cluster0-shard-00-02-q1k7y.mongodb.net:27017/test?ssl=true&replicaSet=Cluster0-shard-0&authSource=admin"
        );

        mongoClient = new MongoClient(uri);

    }

    public void close(){
        mongoClient.close();
    }

    public int getCounter() {
        int result = 0;

        DB db = mongoClient.getDB("testdb");
        DBCollection dbCollection = db.getCollection("params");
//        table.drop();

        DBObject obj = dbCollection.findOne();
        Set<String> keys = obj.keySet();
        Iterator iterator = keys.iterator();

        while(iterator.hasNext()){
            String key = (String) iterator.next();
//            System.out.println("key: " + key);
            Object value = obj.get(key);
//            System.out.println("value: " + value);
            if (key.equals("int")){
                try{
                    result = Integer.parseInt(value.toString());
                    System.out.println("Get count from db: " + result);
                } catch (Exception ex) {
                    System.err.println("Error while getting counter from db");
                    ex.printStackTrace();
                }

            }
        }


        return result;
    }

    public void setCounter(int counter) {

        DB db = mongoClient.getDB("testdb");

        DBCollection table = db.getCollection("params");

        BasicDBObject query = new BasicDBObject();
        query.put("name", "counter");
        DBCursor cursor = table.find(query);

        if (cursor.hasNext()){

            BasicDBObject query2 = new BasicDBObject();
            query.put("name", "counter");

            BasicDBObject newDocument = new BasicDBObject();
            newDocument.put("name", "counter");
            newDocument.put("int", counter);

            BasicDBObject updateObj = new BasicDBObject();
            updateObj.put("$set", newDocument);

            table.update(query, updateObj);

        } else {
            BasicDBObject document = new BasicDBObject();
            document.put("name", "counter");
            document.put("int", counter);
            table.insert(document);
        }


        while (cursor.hasNext()) {
            System.out.println(cursor.next());
        }

        // search document where name="mkyong" and update it with new values

        Counter = counter;
    }
}
