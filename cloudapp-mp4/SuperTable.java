import java.io.IOException;

import org.apache.hadoop.conf.Configuration;

import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;

import org.apache.hadoop.hbase.TableName;

// import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;

import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;

import org.apache.hadoop.hbase.util.Bytes;

public class SuperTable{

   public static void main(String[] args) throws IOException {

      // Instantiate Configuration class
      Configuration con = HBaseConfiguration.create();

      // Instaniate HBaseAdmin class
      Connection connection = ConnectionFactory.createConnection(con);
      Admin admin = connection.getAdmin();
      // HBaseAdmin admin = new HBaseAdmin(con);

      // Instantiate table descriptor class
      HTableDescriptor tableDescriptor = new HTableDescriptor(TableName.valueOf("powers"));

      // Add column families to table descriptor
      tableDescriptor.addFamily(new HColumnDescriptor("personal"));
      tableDescriptor.addFamily(new HColumnDescriptor("professional"));

      // Execute the table through admin
      admin.createTable(tableDescriptor);
      System.out.println(" Table created ");

      // Instantiating HTable class
      Table hTable = connection.getTable(TableName.valueOf("powers"));
      // HTable hTable = new HTable(config, "emp");

      // Repeat these steps as many times as necessary



         // Instantiating Put class
         // Hint: Accepts a row name
         Put p = new Put(Bytes.toBytes("row1"));

         // Add values using add() method
         // Hints: Accepts column family name, qualifier/row name ,value
         p.addColumn(Bytes.toBytes("personal"),
         Bytes.toBytes("hero"),Bytes.toBytes("superman"));

         p.addColumn(Bytes.toBytes("personal"),
         Bytes.toBytes("power"),Bytes.toBytes("strength"));

         p.addColumn(Bytes.toBytes("professional"),Bytes.toBytes("name"),
         Bytes.toBytes("clark"));

         p.addColumn(Bytes.toBytes("professional"),Bytes.toBytes("xp"),
         Bytes.toBytes("100"));

         // Instantiating Put class
         // Hint: Accepts a row name
         Put p2 = new Put(Bytes.toBytes("row2"));

         // Add values using add() method
         // Hints: Accepts column family name, qualifier/row name ,value
         p2.addColumn(Bytes.toBytes("personal"),
         Bytes.toBytes("hero"),Bytes.toBytes("batman"));

         p2.addColumn(Bytes.toBytes("personal"),
         Bytes.toBytes("power"),Bytes.toBytes("money"));

         p2.addColumn(Bytes.toBytes("professional"),Bytes.toBytes("name"),
         Bytes.toBytes("bruce"));

         p2.addColumn(Bytes.toBytes("professional"),Bytes.toBytes("xp"),
         Bytes.toBytes("50"));

         // Instantiating Put class
         // Hint: Accepts a row name
         Put p3 = new Put(Bytes.toBytes("row3"));

         // Add values using add() method
         // Hints: Accepts column family name, qualifier/row name ,value
         p3.addColumn(Bytes.toBytes("personal"),
         Bytes.toBytes("hero"),Bytes.toBytes("wolverine"));

         p3.addColumn(Bytes.toBytes("personal"),
         Bytes.toBytes("power"),Bytes.toBytes("healing"));

         p3.addColumn(Bytes.toBytes("professional"),Bytes.toBytes("name"),
         Bytes.toBytes("logan"));

         p3.addColumn(Bytes.toBytes("professional"),Bytes.toBytes("xp"),
         Bytes.toBytes("75"));

      // Save the table
      hTable.put(p);
      hTable.put(p2);
      hTable.put(p3);
      System.out.println("data inserted");

      // Close table

      // Instantiate the Scan class
      Scan scan = new Scan();

      // Scan the required columns
      scan.addColumn(Bytes.toBytes("personal"), Bytes.toBytes("hero"));

      // Get the scan result
      ResultScanner scanner = hTable.getScanner(scan);

      // Read values from scan result
      // Print scan result
      for (Result result = scanner.next(); result != null; result = scanner.next()) {
         System.out.println("" + result);
      }

      // Close the scanner
      scanner.close();

      // Htable closer
      hTable.close();
   }
}
