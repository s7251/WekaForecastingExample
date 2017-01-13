package main;

import java.io.*;
import java.util.List;
import weka.core.Instances;
import weka.classifiers.functions.GaussianProcesses;
import weka.classifiers.evaluation.NumericPrediction;
import weka.classifiers.timeseries.WekaForecaster;


/**
 * Example of using the time series forecasting API. To compile and
 * run the CLASSPATH will need to contain:
 *
 * weka.jar (from your weka distribution)
 * pdm-timeseriesforecasting-ce-TRUNK-SNAPSHOT.jar (from the time series package)
 * jcommon-1.0.14.jar (from the time series package lib directory)
 * jfreechart-1.0.13.jar (from the time series package lib directory)
 */
public class WekaForecastingExample {

  public static void main(String[] args) {
    try {
      // path to the Australian wine data included with the time series forecasting
      // package
      String pathToWineData = "c:" + File.separator + "account_balance.arff";
System.out.print(pathToWineData);
      // load the wine data
      Instances wine = new Instances(new BufferedReader(new FileReader(pathToWineData)));

      // new forecaster
      WekaForecaster forecaster = new WekaForecaster();

      // set the targets we want to forecast. This method calls
      // setFieldsToLag() on the lag maker object for us
      forecaster.setFieldsToForecast("month_balance");

      // default underlying classifier is SMOreg (SVM) - we'll use
      // gaussian processes for regression instead
      //forecaster.setBaseForecaster(new GaussianProcesses());

      forecaster.getTSLagMaker().setTimeStampField("Date"); // date time stamp
      forecaster.getTSLagMaker().setMinLag(1);
      forecaster.getTSLagMaker().setMaxLag(12); // monthly data

      // add a month of the year indicator field
      forecaster.getTSLagMaker().setAddMonthOfYear(true);

      // add a quarter of the year indicator field
      forecaster.getTSLagMaker().setAddQuarterOfYear(true);

      // build the model
      forecaster.buildForecaster(wine, System.out);

      // prime the forecaster with enough recent historical data
      // to cover up to the maximum lag. In our case, we could just supply
      // the 12 most recent historical instances, as this covers our maximum
      // lag period
      forecaster.primeForecaster(wine);

      // forecast for 12 units (months) beyond the end of the
      // training data
      List<List<NumericPrediction>> forecast = forecaster.forecast(24, System.out);

      // output the predictions. Outer list is over the steps; inner list is over
      // the targets
      for (int i = 0; i < 24; i++) {
        List<NumericPrediction> predsAtStep = forecast.get(i);
        for (int j = 0; j < 1; j++) {
          NumericPrediction predForTarget = predsAtStep.get(j);
          System.out.print("" + predForTarget.predicted() + " ");
        }
        System.out.println();
      }

      // we can continue to use the trained forecaster for further forecasting
      // by priming with the most recent historical data (as it becomes available).
      // At some stage it becomes prudent to re-build the model using current
      // historical data.

    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}

//import weka.experiment.Stats;
//import weka.core.AttributeStats;
//import weka.core.Instance;
//import weka.core.Instances;
//import weka.core.converters.ConverterUtils.DataSource;
//
//public class WekaForecastingExample {
//  public static void main(String args[]) throws Exception{
//      //load dataset
//      DataSource source = new DataSource("C:/account_balance.arff");
//      //get instances object 
//      Instances data = source.getDataSet();
//      //set class index .. as the last attribute
//      if (data.classIndex() == -1) {
//         data.setClassIndex(data.numAttributes() - 1);
//      }
//      //get number of attributes (notice class is not counted)
//      int numAttr = data.numAttributes() - 1;
//      for (int i = 0; i < numAttr; i++) {
//          //check if current attr is of type nominal
//          if (data.attribute(i).isNominal()) {
//              System.out.println("The "+i+"th Attribute is Nominal"); 
//              //get number of values
//              int n = data.attribute(i).numValues();
//              System.out.println("The "+i+"th Attribute has: "+n+" values");
//          }           
//
//          //get an AttributeStats object
//          AttributeStats as = data.attributeStats(i);
//          int dC = as.distinctCount;
//          System.out.println("The "+i+"th Attribute has: "+dC+" distinct values");
//
//          //get a Stats object from the AttributeStats
//          if (data.attribute(i).isNumeric()){
//              System.out.println("The "+i+"th Attribute is Numeric"); 
//              Stats s = as.numericStats;
//              System.out.println("The "+i+"th Attribute has min value: "+s.min+" and max value: "+s.max+" and mean value: "+s.mean+" and stdDev value: "+s.stdDev );
//          }
//
//  }
//
//
//  }
//}

//import java.io.File;
//import java.util.List;
//
//import weka.classifiers.evaluation.NumericPrediction;
//import weka.classifiers.functions.GaussianProcesses;
//import weka.classifiers.functions.LinearRegression;
//import weka.classifiers.timeseries.WekaForecaster;
//import weka.core.Attribute;
//import weka.core.DenseInstance;
//import weka.core.FastVector;
//import weka.core.Instances;
//
///**
//* Example of using the time series forecasting API. To compile and run the
//* CLASSPATH will need to contain:
//*
//* weka.jar (from your weka distribution)
//* pdm-timeseriesforecasting-ce-TRUNK-SNAPSHOT.jar (from the time series
//* package) jcommon-1.0.14.jar (from the time series package lib directory)
//* jfreechart-1.0.13.jar (from the time series package lib directory)
//*/
//public class WekaForecastingExample {
//
//	public static void main(String[] args) {
//		try {
//			// path to the Australian wine data included with the time series
//			// forecasting
//			// package
//			String pathToWineData = weka.core.WekaPackageManager.PACKAGES_DIR.toString() + File.separator
//					+ "timeseriesForecasting" + File.separator + "sample-data" + File.separator + "airline.arff";
//
//			// load the wine data
//
//			Attribute Attribute1 = new Attribute("passenger_numbers");
//			Attribute Attribute2 = new Attribute("Date", "yyyy-MM-dd");
//
//			// Make the feature vector
//			FastVector fvWekaAttributes = new FastVector(2);
//			fvWekaAttributes.addElement(Attribute1);
//			fvWekaAttributes.addElement(Attribute2);
//
//			// Create an empty training set
//			Instances wine = new Instances("airline_passengers", fvWekaAttributes, 10);
//
//			// Set class index
//			wine.setClassIndex(0);
//
//			double[] attValues = new double[wine.numAttributes()];
//			attValues[0] = 1;
//			attValues[1] = wine.attribute("Date").parseDate("2014-01-01");
//			wine.add(new DenseInstance(1.0, attValues));
//
//			attValues = new double[wine.numAttributes()];
//			attValues[0] = 10;
//			attValues[1] = wine.attribute("Date").parseDate("2014-02-01");
//			wine.add(new DenseInstance(1.0, attValues));
//
//			attValues = new double[wine.numAttributes()];
//			attValues[0] = 20;
//			attValues[1] = wine.attribute("Date").parseDate("2014-03-01");
//			wine.add(new DenseInstance(1.0, attValues));
//
//			attValues = new double[wine.numAttributes()];
//			attValues[0] = 45;
//			attValues[1] = wine.attribute("Date").parseDate("2014-04-01");
//			wine.add(new DenseInstance(1.0, attValues));
//
//			attValues = new double[wine.numAttributes()];
//			attValues[0] = 69;
//			attValues[1] = wine.attribute("Date").parseDate("2014-05-01");
//			wine.add(new DenseInstance(1.0, attValues));
//
//			attValues = new double[wine.numAttributes()];
//			attValues[0] = 100;
//			attValues[1] = wine.attribute("Date").parseDate("2014-06-01");
//			wine.add(new DenseInstance(1.0, attValues));
//
//			attValues = new double[wine.numAttributes()];
//			attValues[0] = 112;
//			attValues[1] = wine.attribute("Date").parseDate("2014-07-01");
//			wine.add(new DenseInstance(1.0, attValues));
//			
//			attValues = new double[wine.numAttributes()];
//			attValues[0] = 157;
//			attValues[1] = wine.attribute("Date").parseDate("2014-08-01");
//			wine.add(new DenseInstance(1.0, attValues));
//
//			attValues = new double[wine.numAttributes()];
//			attValues[0] = 180;
//			attValues[1] = wine.attribute("Date").parseDate("2014-09-01");
//			wine.add(new DenseInstance(1.0, attValues));
//			
//			attValues = new double[wine.numAttributes()];
//			attValues[0] = 200;
//			attValues[1] = wine.attribute("Date").parseDate("2014-10-01");
//			wine.add(new DenseInstance(1.0, attValues));
//			
//			attValues = new double[wine.numAttributes()];
//			attValues[0] = 240;
//			attValues[1] = wine.attribute("Date").parseDate("2014-11-01");
//			wine.add(new DenseInstance(1.0, attValues));
//			
//			attValues = new double[wine.numAttributes()];
//			attValues[0] = 240;
//			attValues[1] = wine.attribute("Date").parseDate("2014-12-01");
//			wine.add(new DenseInstance(1.0, attValues));
//			//
//			attValues = new double[wine.numAttributes()];
//			attValues[0] = 280;
//			attValues[1] = wine.attribute("Date").parseDate("2015-01-01");
//			wine.add(new DenseInstance(1.0, attValues));
//			
//			attValues = new double[wine.numAttributes()];
//			attValues[0] = 299;
//			attValues[1] = wine.attribute("Date").parseDate("2015-02-01");
//			wine.add(new DenseInstance(1.0, attValues));
//			
//			attValues = new double[wine.numAttributes()];
//			attValues[0] = 311;
//			attValues[1] = wine.attribute("Date").parseDate("2015-03-01");
//			wine.add(new DenseInstance(1.0, attValues));
//			
//			attValues = new double[wine.numAttributes()];
//			attValues[0] = 356;
//			attValues[1] = wine.attribute("Date").parseDate("2015-04-01");
//			wine.add(new DenseInstance(1.0, attValues));
//			
//			attValues = new double[wine.numAttributes()];
//			attValues[0] = 380;
//			attValues[1] = wine.attribute("Date").parseDate("2015-05-01");
//			wine.add(new DenseInstance(1.0, attValues));
//			
//			attValues = new double[wine.numAttributes()];
//			attValues[0] = 400;
//			attValues[1] = wine.attribute("Date").parseDate("2015-06-01");
//			wine.add(new DenseInstance(1.0, attValues));
//			
//			attValues = new double[wine.numAttributes()];
//			attValues[0] = 412;
//			attValues[1] = wine.attribute("Date").parseDate("2015-07-01");
//			wine.add(new DenseInstance(1.0, attValues));
//			
//			attValues = new double[wine.numAttributes()];
//			attValues[0] = 430;
//			attValues[1] = wine.attribute("Date").parseDate("2015-08-01");
//			wine.add(new DenseInstance(1.0, attValues));
//			
//			attValues = new double[wine.numAttributes()];
//			attValues[0] = 460;
//			attValues[1] = wine.attribute("Date").parseDate("2015-09-01");
//			wine.add(new DenseInstance(1.0, attValues));
//			
//			attValues = new double[wine.numAttributes()];
//			attValues[0] = 480;
//			attValues[1] = wine.attribute("Date").parseDate("2015-10-01");
//			wine.add(new DenseInstance(1.0, attValues));
//			
//			attValues = new double[wine.numAttributes()];
//			attValues[0] = 500;
//			attValues[1] = wine.attribute("Date").parseDate("2015-11-01");
//			wine.add(new DenseInstance(1.0, attValues));
//			
//			attValues = new double[wine.numAttributes()];
//			attValues[0] = 511;
//			attValues[1] = wine.attribute("Date").parseDate("2015-12-01");
//			wine.add(new DenseInstance(1.0, attValues));
//			
//			// Instances wine = new Instances(new BufferedReader(new
//			// FileReader(pathToWineData)));
//
//			// new forecaster
//			WekaForecaster forecaster = new WekaForecaster();
//
//			// set the targets we want to forecast. This method calls
//			// setFieldsToLag() on the lag maker object for us
//			forecaster.setFieldsToForecast("passenger_numbers");
//
//			// default underlying classifier is SMOreg (SVM) - we'll use
//			// gaussian processes for regression instead
//			//forecaster.setBaseForecaster(new GaussianProcesses());
//			//forecaster.setBaseForecaster(new LinearRegression());
//			
//
//			forecaster.getTSLagMaker().setTimeStampField("Date"); // date time
//																	// stamp
//			forecaster.getTSLagMaker().setMinLag(1);
//			forecaster.getTSLagMaker().setMaxLag(12); // monthly data
//
//			// add a month of the year indicator field
//			forecaster.getTSLagMaker().setAddMonthOfYear(false);
//
//			// add a quarter of the year indicator field
//			forecaster.getTSLagMaker().setAddQuarterOfYear(true);
//
//			// build the model
//			forecaster.buildForecaster(wine, System.out);
//
//			// prime the forecaster with enough recent historical data
//			// to cover up to the maximum lag. In our case, we could just supply
//			// the 12 most recent historical instances, as this covers our
//			// maximum
//			// lag period
//			forecaster.primeForecaster(wine);
//
//			// forecast for 12 units (months) beyond the end of the
//			// training data
//			List<List<NumericPrediction>> forecast = forecaster.forecast(24, System.out);
//
//			// output the predictions. Outer list is over the steps; inner list
//			// is over
//			// the targets
//			for (int i = 0; i < 24; i++) {
//				List<NumericPrediction> predsAtStep = forecast.get(i);
//				for (int j = 0; j < 1; j++) {
//					NumericPrediction predForTarget = predsAtStep.get(j);
//					System.out.print("" + predForTarget.predicted() + " ");
//				}
//				System.out.println();
//			}
//
//			// we can continue to use the trained forecaster for further
//			// forecasting
//			// by priming with the most recent historical data (as it becomes
//			// available).
//			// At some stage it becomes prudent to re-build the model using
//			// current
//			// historical data.
//
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
//	}
//
//}
