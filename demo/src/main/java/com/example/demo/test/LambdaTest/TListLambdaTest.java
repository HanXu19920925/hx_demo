package com.example.demo.test.LambdaTest;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import lombok.Data;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * List<T> Lambda表达式
 * @author hanxu
 */
@RestController
@RequestMapping("/lambda")
@Api(tags = "List<T> Lambda表达式")
public class TListLambdaTest {

    //保留两位小数点
    private static final DecimalFormat df = new DecimalFormat("0.00");

    @Data
    // 学生数据结构
    class StudentDTO implements Serializable {

        private static final long serialVersionUID = -7716352032236707189L;

        public StudentDTO() {}

        public StudentDTO(Long id, String code, String name, String sex, Double scope, Integer size, List<Course> learningCources) {
            this.id = id;
            this.code = code;
            this.name = name;
            this.sex = sex;
            this.scope = scope;
            this.size = size;
            this.learningCources = learningCources;
        }

        /**
         * id
         */
        private Long id;

        /**
         * 学号
         */
        private String code;

        /**
         * 姓名
         */
        private String name;

        /**
         * 性别
         */
        private String sex;

        /**
         * 分数
         */
        private Double scope;

        /**
         * 座号
         */
        private Integer size;

        /**
         * 课程
         */
        private List<Course> learningCources;
    }

    @Data
    // 课程数据结构
    class Course implements Serializable {

        private static final long serialVersionUID = 2896201730223729591L;

        /**
         * 课程ID
         */
        private Long id;

        /**
         * 课程名称
         */
        private String name;

        /**
         * 学费
         */
        private BigDecimal amount;

        public Course(Long id, String name, BigDecimal amount) {
            this.id = id;
            this.name = name;
            this.amount = amount;
        }
    }

    // 初始化数据
    public List<StudentDTO> studentsList(){
        List<StudentDTO> studentsList = new ArrayList<>();
        {
            studentsList.add(new StudentDTO(1L,"W199","小美","女人",100D,1,new ArrayList<Course>(){
                {
                    // 添加学生学习的课程
                    add(new Course(300L,"语文", new BigDecimal(10)));
                    add(new Course(301L,"数学", new BigDecimal(20)));
                    add(new Course(302L,"英语", new BigDecimal(30)));
                }
            }));
            studentsList.add(new StudentDTO(2L,"W25","小美","女人",100D,2,Lists.newArrayList()));
            studentsList.add(new StudentDTO(3L,"W3","小名","男人",90D,3,new ArrayList<Course>(){
                {
                    add(new Course(300L,"语文", new BigDecimal(40)));
                    add(new Course(304L,"体育", new BigDecimal(50)));
                }
            }));
            studentsList.add(new StudentDTO(4L,"W1","小蓝","男人",10D,4,new ArrayList<Course>(){
                {
                    add(new Course(301L,"数学", new BigDecimal(60)));
                    add(new Course(305L,"美术", new BigDecimal(70)));
                }
            }));
        }
        return studentsList;
    }

    @GetMapping("/test")
    public void test() {
        List<StudentDTO> studentsList = studentsList();
        System.out.println("查询所有学生信息{}" + JSON.toJSONString(studentsList));
        //[{"code":"W199","id":1,"learningCources":[{"amount":10,"id":300,"name":"语文"},{"amount":20,"id":301,"name":"数学"},{"amount":30,"id":302,"name":"英语"}],"name":"小美","scope":100.0,"sex":"女人","size":1},{"code":"W25","id":2,"learningCources":[],"name":"小美","scope":100.0,"sex":"女人","size":2},{"code":"W3","id":3,"learningCources":[{"amount":40,"id":300,"name":"语文"},{"amount":50,"id":304,"name":"体育"}],"name":"小名","scope":90.0,"sex":"男人","size":3},{"code":"W1","id":4,"learningCources":[{"amount":60,"id":301,"name":"数学"},{"amount":70,"id":305,"name":"美术"}],"name":"小蓝","scope":10.0,"sex":"男人","size":4}]

        //ForEach
        //逐条遍历和批量修改
        studentsList.forEach(o -> {
            System.out.println("查询所有学生信息，逐条遍历，StudentDTO {}" + o);
            o.setSex("男人");
        });
        System.out.println("查询所有学生信息，批量修改性别为“男人”，List<StudentDTO> {}" + JSON.toJSONString(studentsList));

        //Filter
        //只要满足Filter表达式的数据就可以留下来，不满足的数据被过滤掉
        List<String> strList = studentsList.stream().map(StudentDTO::getName).collect(Collectors.toList());
        String strName = strList.stream().filter(x -> StringUtils.isNotEmpty(x)).collect(Collectors.joining("-"));
        System.out.println("查询学生姓名（未去重），字符串拼接{}" + strName);
        List<StudentDTO> stuList = studentsList.stream().filter(str -> StringUtils.equals(str.getName(), "小美")).collect(Collectors.toList());
        System.out.println("只查询小美的相关信息，List<StudentDTO> {}" + JSON.toJSONString(stuList));
        System.out.println("查询有无学生小天，有返回true，无返回false {}" + studentsList.stream().filter(str -> StringUtils.equals(str.getName(), "小天")).findAny().isPresent());

        //MapToInt
        //mapToInt方法功能和map方法一样，只不过mapToInt返回的结果已经没有泛型，已经明确是int类型的流了
        List<Integer> ids = studentsList.stream().mapToInt(s->Integer.valueOf(s.getId()+"")).mapToObj(s->s).collect(Collectors.toList());
        System.out.println("查询所有学生主键，List<Integer> {}" + JSON.toJSONString(ids));
        double scopeSum = studentsList.stream().mapToDouble(s->s.getScope()).sum();
        System.out.println("查询所有学生总分{}" + scopeSum);
        IntSummaryStatistics stat = studentsList.stream().mapToInt(x -> x.getScope().intValue()).summaryStatistics();
        System.out.println("所有学生最高分max{}" + stat.getMax());
        System.out.println("所有学生最低分min{}" + stat.getMin());
        System.out.println("所有学生总分sum{}" + stat.getSum());
        System.out.println("所有学生平均分average{}" + stat.getAverage());

        //flatMap
        //map返回List<List<T>>格式，flatMap返回List<T>格式
        List<List<Course>> courseListToList = studentsList.stream().map(s->s.getLearningCources()).collect(Collectors.toList());
        System.out.println("查询所有学生的学习课程，List<List<Course>> {}" + JSON.toJSONString(courseListToList));

        //Distinct
        //distinct方法去重功能
        List<String> nameList = studentsList.stream().map(StudentDTO::getName).distinct().collect(Collectors.toList());
        System.out.println("去重后的学生名单，List<String> {}" + JSON.toJSONString(nameList));
        List<StudentDTO> property = studentsList.stream().distinct().collect(Collectors.toList());
        System.out.println("去重后的学生信息集合，根据整体元素去重，List<StudentDTO> {}" + JSON.toJSONString(property));
        List<StudentDTO> oneProperty = studentsList.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(StudentDTO::getName))), ArrayList::new));
        System.out.println("去重后的学生信息集合，根据单个元素去重，List<StudentDTO> {}" + JSON.toJSONString(oneProperty));
        List<StudentDTO> muchProperty = studentsList.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(s -> s.getName() + ";" + s.getId()))), ArrayList::new));
        System.out.println("去重后的学生信息集合，根据两个元素去重，List<StudentDTO> {}"+ JSON.toJSONString(muchProperty));
        System.out.println("根据学生姓名去重后的学生个数{}" + studentsList.stream().map(StudentDTO::getName).distinct().count());

        //Sorted
        //Sorted方法提供了排序的功能，并且允许我们自定义排序
        List<String> ascStr = studentsList.stream().map(StudentDTO::getCode).sorted().collect(Collectors.toList());
        System.out.println("查询所有学生编码-自然排序，List<String> {}" + JSON.toJSONString(ascStr));
        List<String> descStr = studentsList.stream().map(StudentDTO::getCode).sorted(Comparator.reverseOrder()).collect(Collectors.toList());
        System.out.println("查询所有学生编码-反自然排序，List<String> {}" + JSON.toJSONString(descStr));
        List<StudentDTO> ascList = studentsList.stream().sorted(Comparator.comparing(StudentDTO::getId)).collect(Collectors.toList());
        System.out.println("查询所有学生信息，id升序，List<StudentDTO> {}" + JSON.toJSONString(ascList));
        List<StudentDTO> descList = studentsList.stream().sorted(Comparator.comparing(StudentDTO::getId).reversed()).collect(Collectors.toList());
        System.out.println("查询所有学生信息，id降序，List<StudentDTO> {}" + JSON.toJSONString(descList));
        List<StudentDTO> listStu = studentsList.stream().sorted(Comparator.comparing(StudentDTO::getCode).thenComparing(StudentDTO::getId)).collect(Collectors.toList());
        System.out.println("查询所有学生信息，多条件排序，List<StudentDTO> {}" + JSON.toJSONString(listStu));

        //groupingBy，List转Map
        //groupingBy是能够根据字段进行分组，toMap是把List的数据格式转化成Map格式
        Map<String, List<StudentDTO>> nameMap = studentsList.stream().collect(Collectors.groupingBy(StudentDTO::getName));
        System.out.println("学生根据名字进行分类，Map<String,List<StudentDTO>> {}" + JSON.toJSONString(nameMap));
        Map<String, List<StudentDTO>> nameNotNullMap = studentsList.stream().filter(v -> StringUtils.isNotEmpty(v.getName())).collect(Collectors.groupingBy(StudentDTO::getName));
        System.out.println("学生根据名字进行分类，非空过滤，Map<String,List<StudentDTO>> {}" + JSON.toJSONString(nameNotNullMap));
        Map<String, Long> nameCountMap = studentsList.stream().collect(Collectors.groupingBy((StudentDTO bean)->{
            return bean.getName();
        },Collectors.counting()));
        System.out.println("学生根据名字进行分组统计，返回的map中key为名字，value为元素的数量，Map<String, Long> {}" + nameCountMap);
        Map<String, Set<String>> nameRepeatMap = studentsList.stream().collect(Collectors.groupingBy(StudentDTO::getName, Collectors.mapping(StudentDTO::getCode,Collectors.toSet())));
        System.out.println("统计姓名重名结果，Map<String, Set<String>> {}" + nameRepeatMap);
        //第一个入参表示 map 中 key 的取值
        //第二个入参表示 map 中 value 的取值
        //第三个入参表示，如果前后的 key 是相同的，是覆盖还是不覆盖，(s1,s2)->s1 表示不覆盖，(s1,s2)->s2 表示覆盖
        Map<String, StudentDTO> stuMap = studentsList.stream().collect(Collectors.toMap(s->s.getCode(),s->s,(s1,s2)->s1));
        System.out.println("学生转化成学号结果，Map<String, StudentDTO> {}" + JSON.toJSONString(stuMap));
        Map<Long, Double> map = studentsList.stream().collect(Collectors.toMap(StudentDTO::getId, StudentDTO::getScope, (k1, k2) -> k2));
        System.out.println("学生转化成id，分数结果，Map<Long, Double> {}" + JSON.toJSONString(map));
        //多重分组
        Map<String, Map<Integer, List<StudentDTO>>> paramMap = studentsList.stream().collect(Collectors.groupingBy(t->t.getName(),Collectors.groupingBy(t->Integer.valueOf(t.getId()+""))));
        System.out.println("学生根据名字和ID进行多重分组，Map<String, Map<Integer, List<StudentDTO>>> {}" + JSON.toJSONString(paramMap));
        //分组并计算综合
        Map<String, Map<Integer, LongSummaryStatistics>> summaryMap = studentsList.stream().collect(Collectors.groupingBy(t->t.getName(),Collectors.groupingBy(t->Integer.valueOf(t.getId()+""),Collectors.summarizingLong(StudentDTO::getSize))));
        System.out.println("学生根据名字和ID进行多重分组并计算综合，Map<String, Map<Integer, LongSummaryStatistics>> {}" + JSON.toJSONString(summaryMap));

        //FindFirst
        //findFirst表示匹配到第一个满足条件的值就返回
        System.out.println("同学中有两个叫小美的，这里匹配到第一个就返回，小美同学的ID{}" + studentsList.stream().filter(s-> StringUtils.equals(s.getName(),"小美")).findFirst().get().getId());
        //防止空指针
        System.out.println("orElse表示如果findFirst返回null的话，就返回orElse里的内容，小天同学的ID{}" + studentsList.stream().filter(s->StringUtils.equals(s.getName(),"小天")).findFirst().orElse(new StudentDTO()).getId());

        Optional<StudentDTO> student = studentsList.stream().filter(s->StringUtils.equals(s.getName(),"小天")).findFirst();
        //isPresent为true表示value != null，即student.get() != null
        if(student.isPresent()){
            System.out.println("小天同学的ID{}" + student.get().getId());
            return;
        }
        System.out.println("找不到姓名为小天的同学");

        //Reduce
        //reduce方法允许我们在循环里面叠加计算值
        System.out.println("计算学生的总成绩{}" + studentsList.stream().map(StudentDTO::getScope).reduce((scope1,scope2) -> scope1+scope2).orElse(0D));
        System.out.println("第一个参数表示成绩的基数，会从100开始加，计算学生的总成绩{}" + studentsList.stream().map(StudentDTO::getScope).reduce(100D,(scope1,scope2) -> scope1+scope2));

        //Peek
        //peek方法很简单，我们在peek方法里面做任意没有返回值的事情，比如打印日志
        studentsList.stream().map(StudentDTO::getCode).peek(s -> System.out.println("当前循环的学号是{}"+ s)).collect(Collectors.toList());

        //Limit
        //limit方法会限制输出值个数，入参是限制的个数大小
        System.out.println("查询前两个同学的ID{}" + studentsList.stream().map(StudentDTO::getId).limit(2L).collect(Collectors.toList()));

        //Max，Min
        //通过max、min方法，可以获取集合中最大、最小的对象
        StudentDTO dtoMax = studentsList.stream().max(Comparator.comparing(s -> s.getScope())).get();
        System.out.println("查询学生中成绩最高的学生信息，StudentDTO {}" + JSON.toJSONString(dtoMax));
        StudentDTO dtoMin = studentsList.stream().min(Comparator.comparing(s -> s.getScope())).get();
        System.out.println("查询学生中成绩最低的学生信息，StudentDTO {}" + JSON.toJSONString(dtoMin));

        //Map
        //map方法可以让我们进行一些流的转化，比如原来流中的元素是A，通过map操作，可以使返回的流中的元素是B，以及对数据进行计算
        List<String> codeList = studentsList.stream().map(StudentDTO::getCode).collect(Collectors.toList());
        System.out.println("查询所有学生的学号，List<String> {}" + JSON.toJSONString(codeList));
        List<Course> courses = studentsList.stream().flatMap(s->s.getLearningCources().stream()).collect(Collectors.toList());
        System.out.println("查询所有课程信息，List<Course> {}" + JSON.toJSONString(courses));
        System.out.println("查询所有课程的学费求和，保留两位小数并转成string，df.format(val); {}" + courses.stream().map(Course::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2,BigDecimal.ROUND_DOWN));
        System.out.println("查询所有课程的学费最大值，保留两位小数并转成string，df.format(val); {}" + courses.stream().max((u1, u2) -> u1.getAmount().compareTo(u2.getAmount())).get().getAmount());
        System.out.println("查询所有课程的学费最小值，保留两位小数并转成string，df.format(val); {}" + courses.stream().min((u1, u2) -> u1.getAmount().compareTo(u2.getAmount())).get().getAmount());
        System.out.println("查询所有课程的学费平均值，保留两位小数并转成string，df.format(val); {}" + courses.stream().map(Course::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add).divide(new BigDecimal(courses.size())));
        List<StudentDTO> dtoList = studentsList.stream().map(x -> {
            x.setScope(x.getScope()+10D);
            return x;
        }).collect(Collectors.toList());
        System.out.println("所有学生的成绩都加10，List<StudentDTO> {}" + JSON.toJSONString(dtoList));

        //数组转集合
        String[] str = {"张三","李四"};
        System.out.println("数组转集合，List<String> {}" + Stream.of(str).collect(Collectors.toList()));

        //集合转数组
        List<String> list = new ArrayList<String>() {{
            add("A");
            add("B");
            add("C");
        }};
        System.out.println("集合转数组，String[] {}" + JSON.toJSONString(list.stream().toArray(String[]::new)));
    }
}