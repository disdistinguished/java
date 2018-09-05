package movie;
    import java.util.ArrayList;  
    import java.util.List;  
      
    import org.apache.spark.SparkConf;  
    import org.apache.spark.api.java.JavaPairRDD;  
    import org.apache.spark.api.java.JavaRDD;  
    import org.apache.spark.api.java.JavaSparkContext;  
    import org.apache.spark.api.java.function.DoubleFunction;  
    import org.apache.spark.api.java.function.Function;  
    import org.apache.spark.api.java.function.Function2;  
    import org.apache.spark.api.java.function.VoidFunction;  
    import org.apache.spark.mllib.recommendation.ALS;  
    import org.apache.spark.mllib.recommendation.MatrixFactorizationModel;  
    import org.apache.spark.mllib.recommendation.Rating;  
      
    import scala.Tuple2;  
    import scala.Tuple3;  
      
    /** 
     * Collaborative filtering Эͬ���� alternating least squares (ALS) (������С���˷�(ALS) ) 
     * Title. <br> 
     * Description. 
     * <p> 
     * Version: 1.0 
     * <p> 
     */  
    public class moive_recommend {  
          
        public static void main(String[] args) {  
            // ������ڶ���  
            SparkConf conf = new SparkConf().setAppName("Collaborative Filtering Example").setMaster("local");  
            JavaSparkContext sc = new JavaSparkContext(conf);  
      
            // ������������  
            String path = "J:/ml-latest-small/ratings";  
            JavaRDD<String> data = sc.textFile(path);  
              
            // �����������ݣ����ڴ�����Ҫ��������ʹ�ã�60%����ѵ����20%������֤�����20%���ڲ��ԡ���ʱ���%10���Եõ����Ƶ�10�ȷ֣����������������з�  
            JavaRDD<Tuple2<Integer, Rating>> ratingsTrain_KV = data.map(new Function<String, Tuple2<Integer, Rating>>() {  
      
                @Override  
                public Tuple2<Integer, Rating> call(String line) throws Exception {  
                    String[] fields = line.split(",");  
                    if (fields.length != 4) {  
                        throw new IllegalArgumentException("ÿһ�б�������ֻ��4��Ԫ��");  
                    }  
                    int userId = Integer.parseInt(fields[0]);  
                    int movieId = Integer.parseInt(fields[1]);  
                    double rating = Float.parseFloat(fields[2]);  
                    int timestamp = (int) (Long.parseLong(fields[3])%10);  
                    return new Tuple2<Integer, Rating>(timestamp, new Rating(userId, movieId, rating));  
                }  
      
            });  
      
            System.out.println("get " + ratingsTrain_KV.count() + " ratings from " + ratingsTrain_KV.distinct().count() + "users on " + ratingsTrain_KV.distinct().count() + "movies");  
              
            // �����ҵ���������  
            String mypath = "J:/ml-latest-small/ratings";  
            JavaRDD<String> mydata = sc.textFile(mypath);  
            JavaRDD<Rating> myRatedData_Rating = mydata.map(new Function<String, Rating>() {  
      
                @Override  
                public Rating call(String line) throws Exception {  
                    String[] fields = line.split(",");  
                    if (fields.length != 4) {  
                        throw new IllegalArgumentException("ÿһ�б�������ֻ��4��Ԫ��");  
                    }  
                    int userId = Integer.parseInt(fields[0]);  
                    int movieId = Integer.parseInt(fields[1]);  
                    double rating = Float.parseFloat(fields[2]);  
                    return new Rating(userId, movieId, rating);  
                }  
            });  
              
            //���÷�����  
            int numPartitions = 3;  
              
            //����ֵС��6��60%������������ѵ��  
            JavaRDD<Rating> traningData_Rating = JavaPairRDD.fromJavaRDD(ratingsTrain_KV.filter(new Function<Tuple2<Integer,Rating>, Boolean>() {  
      
                @Override  
                public Boolean call(Tuple2<Integer, Rating> v1) throws Exception {  
                      
                    return v1._1 < 6;  
                }  
            })).values().union(myRatedData_Rating).repartition(numPartitions).cache();  
              
            //����ֵ����6С��8��20%��������������֤  
            JavaRDD<Rating> validateData_Rating = JavaPairRDD.fromJavaRDD(ratingsTrain_KV.filter(new Function<Tuple2<Integer,Rating>, Boolean>() {  
                  
                @Override  
                public Boolean call(Tuple2<Integer, Rating> v1) throws Exception {  
                      
                    return v1._1 >= 6 && v1._1 < 8;  
                }  
            })).values().repartition(numPartitions).cache();  
              
            //����ֵ����8��20%�����������ڲ���  
            JavaRDD<Rating> testData_Rating = JavaPairRDD.fromJavaRDD(ratingsTrain_KV.filter(new Function<Tuple2<Integer,Rating>, Boolean>() {  
                  
                @Override  
                public Boolean call(Tuple2<Integer, Rating> v1) throws Exception {  
                      
                    return v1._1 >= 8;  
                }  
            })).values().cache();  
              
            System.out.println("training data's num : " + traningData_Rating.count() + " validate data's num : " + validateData_Rating.count() + " test data's num : " + testData_Rating.count());  
              
            // Ϊѵ�����ò��� ÿ�ֲ�������2��ֵ������forѭ����һ������8��ѵ��  
            List<Integer> ranks = new ArrayList<Integer>();  
            ranks.add(8);  
            ranks.add(22);  
              
            List<Double> lambdas = new ArrayList<Double>();  
            lambdas.add(0.1);  
            lambdas.add(10.0);  
              
            List<Integer> iters = new ArrayList<Integer>();  
            iters.add(5);  
            iters.add(7);  
              
            // ��ʼ����õ�ģ�Ͳ���   
            MatrixFactorizationModel bestModel = null;  
            double bestValidateRnse = Double.MAX_VALUE;  
            int bestRank = 0;  
            double bestLambda = -1.0;  
            int bestIter = -1;  
              
            for (int i = 0; i < ranks.size(); i++) {  
                for (int j = 0; j < lambdas.size(); j++) {  
                    for (int k = 0; k < iters.size(); k++) {  
                        //ѵ�����ģ��  
                        MatrixFactorizationModel model = ALS.train(JavaRDD.toRDD(traningData_Rating), ranks.get(i), iters.get(i), lambdas.get(i));  
                        //ͨ��У�鼯validateData_Rating��ȡ����Ա�鿴��ģ�͵ĺû��������������������  
                        double validateRnse = variance(model, validateData_Rating, validateData_Rating.count());  
                        System.out.println("validation = " + validateRnse + " for the model trained with rank = " + ranks.get(i) + " lambda = " + lambdas.get(i) + " and numIter" + iters.get(i));  
                          
                        //����õ�ģ��ѵ����������õĲ������б���  
                        if (validateRnse < bestValidateRnse) {  
                            bestModel = model;  
                            bestValidateRnse = validateRnse;  
                            bestRank = ranks.get(i);  
                            bestLambda = lambdas.get(i);  
                            bestIter = iters.get(i);  
                        }  
                    }  
                }  
            }  
              
            //8��ѵ�����ȡ��õ�ģ�ͣ�������õ�ģ�ͼ�ѵ����testData_Rating����ȡ�˷���  
            double testDataRnse = variance(bestModel, testData_Rating, testData_Rating.count());  
            System.out.println("the best model was trained with rank = " + bestRank + " and lambda = " + bestLambda  
                               + " and numIter = " + bestIter + " and Rnse on the test data is " + testDataRnse);  
              
            // ��ȡ���������У�������ƽ��ֵ  
            final double meanRating = traningData_Rating.union(validateData_Rating).mapToDouble(new DoubleFunction<Rating>() {  
                  
                @Override  
                public double call(Rating t) throws Exception {  
                    return t.rating();  
                }  
            }).mean();  
              
            // ����ƽ��ֵ������ɵķ���ֵ  
            double baseLineRnse = Math.sqrt(testData_Rating.mapToDouble(new DoubleFunction<Rating>() {  
                  
                @Override  
                public double call(Rating t) throws Exception {  
                    return (meanRating - t.rating()) * (meanRating - t.rating());  
                }  
            }).mean());  
              
            // ͨ��ģ�ͣ����ݵ���϶������˶���  
            double improvent = (baseLineRnse - testDataRnse) / baseLineRnse * 100;  
            System.out.println("the best model improves the baseline by " + improvent + "%");  
              
            //���ص�Ӱ����  
            String moviepath = "J:/ml-latest-small/movies";  
            JavaRDD<String> moviedata = sc.textFile(moviepath);  
              
            // ����Ӱ��id�����⣬��������Ԫ�����ʽ����  
            JavaRDD<Tuple3<Integer, String, String>> movieList_Tuple = moviedata.map(new Function<String, Tuple3<Integer, String, String>>() {  
      
                @Override  
                public Tuple3<Integer, String, String> call(String line) throws Exception {  
                    String[] fields = line.split(",");  
                    if (fields.length != 3) {  
                        throw new IllegalArgumentException("Each line must contain 3 fields");  
                    }  
                    int id = Integer.parseInt(fields[0]);  
                    String title = fields[1];  
                    String type = fields[2];  
                    return new Tuple3<Integer, String, String>(id, title, type);  
                }  
            });  
              
            // ����Ӱ��id�������Զ�Ԫ�����ʽ����  
            JavaRDD<Tuple2<Integer, String>> movies_Map = movieList_Tuple.map(new Function<Tuple3<Integer,String,String>, Tuple2<Integer,String>>() {  
      
                @Override  
                public Tuple2<Integer, String> call(Tuple3<Integer, String, String> v1) throws Exception {  
                    return new Tuple2<Integer, String>(v1._1(), v1._2());  
                }  
            });  
              
            System.out.println("movies recommond for you:");  
              
            // ��ȡ���������ĵ�Ӱids  
            final List<Integer> movieIds = myRatedData_Rating.map(new Function<Rating, Integer>() {  
      
                @Override  
                public Integer call(Rating v1) throws Exception {  
                    return v1.product();  
                }  
            }).collect();  
              
            // �ӵ�Ӱ������ȥ���ҿ����ĵ�Ӱ����  
            JavaRDD<Tuple2<Integer, String>> movieIdList  = movies_Map.filter(new Function<Tuple2<Integer,String>, Boolean>() {  
      
                @Override  
                public Boolean call(Tuple2<Integer, String> v1) throws Exception {  
                    return !movieIds.contains(v1._1);  
                }  
            });  
              
            // ��װrating�Ĳ�����ʽ��userΪ0��productΪ��Ӱid���з�װ  
            JavaPairRDD<Integer, Integer> recommondList = JavaPairRDD.fromJavaRDD(movieIdList.map(new Function<Tuple2<Integer,String>, Tuple2<Integer,Integer>>() {  
      
                @Override  
                public Tuple2<Integer, Integer> call(Tuple2<Integer, String> v1) throws Exception {  
                    return new Tuple2<Integer, Integer>(0, v1._1);  
                }  
            }));  
              
            //ͨ��ģ��Ԥ���userΪ0�ĸ�product(��Ӱid)�����֣����������ֽ������򣬻�ȡǰ10����Ӱid  
            @SuppressWarnings("unchecked")
			JavaRDD<Tuple2<Integer, Double>> list = (JavaRDD<Tuple2<Integer, Double>>) bestModel.predict(recommondList).sortBy(new Function<Rating, Double>() {  
      
                @Override  
                public Double call(Rating v1) throws Exception {  
                    return v1.rating();  
                }  
            }, false, 1).map(new Function<Rating, Integer>() {  
      
                @Override  
                public Tuple2<Integer, Double> call(Rating v1) throws Exception {  
                    return new Tuple2<Integer,Double>(v1.product(),v1.rating());  
                }  
            }).take(10);  
              
            if (list != null && !list.isEmpty()) {  
                //�ӵ�Ӱ�����й��˳���10����Ӱ��������ӡ  
                movieList_Tuple.filter(new Function<Tuple3<Integer,String,String>, Boolean>() {  
      
                    @Override  
                    public Boolean call(Tuple3<Integer, String, String> v1) throws Exception {  
                        return list.contains(v1._1());  
                    }  
                }).foreach(new VoidFunction<Tuple3<Integer,String,String>>() {  
      
                    @Override  
                    public void call(Tuple3<Integer, String, String> t) throws Exception {  
                        System.out.println("nmovie name --> " + t._2() + " nmovie type --> " + t._3());  
                    }  
                });  
            }  
              
        }  
          
        //����ļ��㷽��  
        public static double variance(MatrixFactorizationModel model, JavaRDD<Rating> predictionData, long n) {  
            //��predictionDataת���ɶ�Ԫ����ʽ���Ա�ѵ��ʹ��  
            JavaRDD<Tuple2<Object, Object>> userProducts = predictionData.map(new Function<Rating, Tuple2<Object, Object>>() {  
      
                public Tuple2<Object, Object> call(Rating r) {  
                    return new Tuple2<Object, Object>(r.user(), r.product());  
                }  
            });  
              
            //ͨ��ģ�Ͷ����ݽ���Ԥ��  
            JavaPairRDD<Tuple2<Integer, Integer>, Double> prediction = JavaPairRDD.fromJavaRDD(model.predict(JavaRDD.toRDD(userProducts)).toJavaRDD().map(new Function<Rating, Tuple2<Tuple2<Integer, Integer>, Double>>() {  
      
                public Tuple2<Tuple2<Integer, Integer>, Double> call(Rating r) {  
                    //System.out.println(r.user()+"..."+r.product()+"..."+r.rating());  
                    return new Tuple2<Tuple2<Integer, Integer>, Double>(new Tuple2<Integer, Integer>(r.user(), r.product()), r.rating());  
                }  
            }));  
              
            //Ԥ��ֵ��ԭֵ������  
            JavaRDD<Tuple2<Double, Double>> ratesAndPreds = JavaPairRDD.fromJavaRDD(predictionData.map(new Function<Rating, Tuple2<Tuple2<Integer, Integer>, Double>>() {  
      
                public Tuple2<Tuple2<Integer, Integer>, Double> call(Rating r) {  
                    //System.out.println(r.user() + "..." + r.product() + "..." + r.rating());  
                    return new Tuple2<Tuple2<Integer, Integer>, Double>(new Tuple2<Integer, Integer>(r.user(), r.product()), r.rating());  
                }  
            })).join(prediction).values();  
              
            //���㷽����ؽ��  
            Double dVar = ratesAndPreds.map(new Function<Tuple2<Double,Double>, Double>() {  
      
                @Override  
                public Double call(Tuple2<Double, Double> v1) throws Exception {  
                    return (v1._1 - v1._2) * (v1._1 - v1._2);  
                }  
            }).reduce(new Function2<Double, Double, Double>() {  
      
                @Override  
                public Double call(Double v1, Double v2) throws Exception {  
                    return v1 + v2;  
                }  
            });  
              
            return Math.sqrt(dVar / n);  
        }  
          
    }  