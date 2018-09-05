/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package scala;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
// $example off:schema_merging$

// $example on:basic_parquet_example$
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.sql.Encoders;
// $example on:schema_merging$
// $example on:json_dataset$
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
// $example off:json_dataset$
// $example off:schema_merging$
// $example off:basic_parquet_example$
import org.apache.spark.sql.SparkSession;

public class JavaSQLDataSourceExample {

  public static class Square implements Serializable {
    private int value;
    private int square;

    public int getValue() {
      return value;
    }

    public void setValue(int value) {
      this.value = value;
    }

    public int getSquare() {
      return square;
    }

    public void setSquare(int square) {
      this.square = square;
    }
  }

  public static class Cube implements Serializable {
    private int value;
    private int cube;

    // Getters and setters...
    public int getValue() {
      return value;
    }

    public void setValue(int value) {
      this.value = value;
    }

    public int getCube() {
      return cube;
    }

    public void setCube(int cube) {
      this.cube = cube;
    }
  }

  public static void main(String[] args) {
    SparkSession spark = SparkSession
      .builder()
      .appName("Java Spark SQL data sources example")
      .config("spark.some.config.option", "some-value")
      .master("local")
      .getOrCreate();

//    runBasicDataSourceExample(spark);
//    runBasicParquetExample(spark);
//    runParquetSchemaMergingExample(spark);
    runJsonDatasetExample(spark);
//    runJdbcDatasetExample(spark);
    spark.stop();
  }

  private static void runBasicDataSourceExample(SparkSession spark) {
//    Dataset<Row> usersDF = spark.read().load("users.parquet");
//    usersDF.printSchema();
//    usersDF.foreach(System.out::println);
//    usersDF.select("name", "favorite_color").write().save("namesAndFavColors.parquet");

//    Dataset<Row> peopleDF =
//      spark.read().format("json").load("people.json");
//    peopleDF.select("name11", "age").write().format("parquet").save("namesAndAges.parquet");
//
    Dataset<Row> sqlDF = spark.sql("SELECT * FROM parquet.`users.parquet`");
    sqlDF.foreach(System.out::println);

    Dataset<Row> sqlDF1 = spark.sql("SELECT * FROM json.`people.json`");
    sqlDF1.foreach(System.out::println);
  }

  private static void runBasicParquetExample(SparkSession spark) {
    Dataset<Row> peopleDF = spark.read().json("people.json");

    // DataFrames can be saved as Parquet files, maintaining the schema information
    peopleDF.write().parquet("people.parquet");

    // Read in the Parquet file created above.
    // Parquet files are self-describing so the schema is preserved
    // The result of loading a parquet file is also a DataFrame
    Dataset<Row> parquetFileDF = spark.read().parquet("people.parquet");

    // Parquet files can also be used to create a temporary view and then used in SQL statements
    parquetFileDF.createOrReplaceTempView("parquetFile");
    Dataset<Row> namesDF = spark.sql("SELECT name11 FROM parquetFile WHERE age BETWEEN 13 AND 19");
    Dataset<String> namesDS = namesDF.map(new MapFunction<Row, String>() {
      public String call(Row row) {
        return "Name: " + row.getString(0);
      }
    }, Encoders.STRING());
    namesDS.show();
  }

  private static void runParquetSchemaMergingExample(SparkSession spark) {
    List<Square> squares = new ArrayList<>();
    for(int value = 1; value <= 5; value++) {
      Square square = new Square();
      square.setValue(value);
      square.setSquare(value * value);
      squares.add(square);
    }

    // Create a simple DataFrame, store into a partition directory
    Dataset<Row> squaresDF = spark.createDataFrame(squares, Square.class);
    squaresDF.write().parquet("data/test_table/key=1");

    List<Cube> cubes = new ArrayList<>();
    for (int value = 6; value <= 10; value++) {
      Cube cube = new Cube();
      cube.setValue(value);
      cube.setCube(value * value * value);
      cubes.add(cube);
    }

    // Create another DataFrame in a new partition directory,
    // adding a new column and dropping an existing column
    Dataset<Row> cubesDF = spark.createDataFrame(cubes, Cube.class);
    cubesDF.write().parquet("data/test_table/key=2");

    // Read the partitioned table
    Dataset<Row> mergedDF = spark.read().option("mergeSchema", true).parquet("data/test_table");
    mergedDF.printSchema();
    mergedDF.foreach(System.out::println);
    // The final schema consists of all 3 columns in the Parquet files together
    // with the partitioning column appeared in the partition directory paths
    // root
    //  |-- value: int (nullable = true)
    //  |-- square: int (nullable = true)
    //  |-- cube: int (nullable = true)
    //  |-- key: int (nullable = true)
  }

  private static void runJsonDatasetExample(SparkSession spark) {
    Dataset<Row> people = spark.read().json("people.json");
    people.printSchema();
    // Creates a temporary view using the DataFrame
    people.createOrReplaceTempView("people");

    // SQL statements can be run by using the sql methods provided by spark
    Dataset<Row> namesDF = spark.sql("SELECT name FROM people WHERE age BETWEEN 13 AND 19");
    namesDF.show();
  }

  private static void runJdbcDatasetExample(SparkSession spark) {
    Dataset<Row> jdbcDF = spark.read()
      .format("jdbc")
      .option("url", "jdbc:postgresql:dbserver")
      .option("dbtable", "schema.tablename")
      .option("user", "username")
      .option("password", "password")
      .load();
  }
}
