package cn.chenzw.excel.magic.core;

import cn.chenzw.excel.magic.core.meta.annotation.*;
import cn.chenzw.excel.magic.core.support.ExcelReader;
import cn.chenzw.excel.magic.core.support.ExcelWriter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.*;
import java.lang.management.ManagementFactory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class App {

    private static final String DEFAULT_XLXS = "user.xlsx";


    public static void main(String[] args) throws IOException {
        handleExcelExport();
        handleExcelImport();
    }

   /* public static void main(String[] args) throws IOException {

        Console console = System.console();
        if (console == null) {
            throw new IllegalStateException("不能使用控制台");
        }

        String oper = console.readLine("请选择: [1:导出; 2:导入]");
        if (StringUtils.containsAny(oper, "1", "2")) {
            if ("1".equals(oper)) {
                handleExcelExport();
            } else if ("2".equals(oper)) {
                handleExcelImport();
            }
        } else {
            System.out.println("请输入1或2!");
        }

    }*/


    private static void handleExcelExport() throws IOException {
        // Console console = System.console();
        //   String rows = console.readLine("请输入要导出的条数: ");

        String rows = "10000";
        if (NumberUtils.isCreatable(rows)) {
            long t1 = System.currentTimeMillis();
            long used1 = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed();
            long lRows = NumberUtils.toLong(rows);
            List<User> users = new ArrayList<>();
            for (int i = 0; i < lRows; i++) {
                User user = new User();
                user.setName("张三" + i);
                user.setAge(i);
                user.setHeight(90.20);
                user.setBirth(Calendar.getInstance().getTime());
                users.add(user);
            }
            long used2 = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed();
            long t2 = System.currentTimeMillis();
            System.out.println("【构建数据】 - 耗时:" + (t2 - t1) + " ms");
            System.out.println("【构建数据】 - 耗费内存:" + (used2 - used1) / 1024 / 1024 + " MB");

            File excelFile = new File(DEFAULT_XLXS);
            ExcelWriter.newInstance().addData(users).write(new FileOutputStream(excelFile));

            long used3 = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed();
            long t3 = System.currentTimeMillis();
            System.out.println("【导出excel】 - 耗时:" + (t3 - t2) + " ms");
            System.out.println("【导出excel】 - 耗费内存:" + (used3 - used2) / 1024 / 1024 + " MB");

        } else {
            System.out.println("请输入整数!");
        }
    }

    private static void handleExcelImport() throws FileNotFoundException {
        File excelFile = new File(DEFAULT_XLXS);
        if (!excelFile.exists()) {
            System.out.println("导入的excel不存在,请先使用导出功能!");
            return;
        }
        long t1 = System.currentTimeMillis();
        long used1 = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed();
        List<User> users = ExcelReader.newInstance(new FileInputStream(excelFile)).read(User.class);
        System.out.println("成功读取:" + users.size() + "条数据");

        long t2 = System.currentTimeMillis();
        long used2 = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed();

        System.out.println("【导入excel】 - 耗时:" + (t2 - t1) + " ms");
        System.out.println("【导入excel】 - 耗费内存:" + (used2 - used1) / 1024 / 1024 + " MB");
    }

    @ExcelImport(sheetIndex = 1, firstDataRow = 2)
    @ExcelExport(sheetName = "用户数据")
    public static class User {

        @ExcelExportColumn(colIndex = 1, title = "姓名")
        @ExcelImportColumn(colIndex = 1)
        private String name;

        @ExcelExportColumn(colIndex = 2, title = "年龄")
        @ExcelImportColumn(colIndex = 2)
        private Integer age;

        @ExcelExportColumn(colIndex = 3, title = "生日", dataFormat = @ExcelDataFormat("yyyy-MM-dd"))
        @ExcelImportColumn(colIndex = 3, dateFormat = "yyyy-MM-dd")
        private Date birth;

        @ExcelExportColumn(colIndex = 4, title = "体重", dataFormat = @ExcelDataFormat("0.00"))
        @ExcelImportColumn(colIndex = 4)
        private Double height;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        public Date getBirth() {
            return birth;
        }

        public void setBirth(Date birth) {
            this.birth = birth;
        }


        public Double getHeight() {
            return height;
        }

        public void setHeight(Double height) {
            this.height = height;
        }

    }
}
