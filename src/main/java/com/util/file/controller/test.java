package com.util.file.controller;


import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.*;


/**
 * @ClassName:
 * @Description: //
 */
public class test {

   public static void main(String[] args) throws ParseException {


       /*File file = new File("E:\\project\\test.xls");
       try {
          List<List<Object>> list =  ReadExcel.readExcel(file);
           for (List<Object> objects : list) {
               for (Object object : objects) {
                   System.out.print(object.toString() + ", ");
               }
           }
       } catch (IOException e) {
           e.printStackTrace();
       }*/

       /*StatOrderRequest request = new StatOrderRequest();
       request.setStartTime(DateUtil.getTodayStartTime());
       request.setEndTime(DateUtil.getTodayEndTime());

       List<StatOrder> statOrderList = statOrderMapper.statOrderCountByDay(request);

       System.out.println("statOrderList :" + statOrderList);*/

       /*String time = "2019-06-04";
       Date date = DateUtil.getDateByString(time, TimeUnitCode.DAY);
       System.out.println("date: "  + date);
*/

      /* MultipartFile file = null;
       DataImportService dataImportService = new DataImportService();
       dataImportService.importCompany(file, null, 73753);*/

     /* String test = "abcd   ";

      String replace = "  ";
       System.out.println("flag: " + "".equals(replace));
       System.out.println("test: " + replace.replaceAll(" ", "").length());

       Set<String> shopConfigSet = new HashSet<>();

      shopConfigSet.add(test);
      System.out.println("contains: " + shopConfigSet.contains("abcd   "));*/

       Set<String> aSet = new HashSet<>();
       String a = "a";
       String b = "ab";
       String c = "abc";
       aSet.add(a);
       aSet.add(b);
       aSet.add(c);

       List<String> bSet = new ArrayList<>();
       String e = "e";
       String f = "f";
       bSet.add(a);
       bSet.add(b);
       bSet.add(c);
       bSet.add(e);
       bSet.add(f);

       aSet.addAll(bSet);
       System.out.println(aSet.size());
   }
}
