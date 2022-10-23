package ocrexcel.demo.fuxin.config;


import cn.binarywang.tools.generator.ChineseAddressGenerator;
import cn.binarywang.tools.generator.ChineseNameGenerator;
import cn.binarywang.tools.generator.base.GenericGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Collections;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@Component
public class PaddleOcr {

    @Value("${paddle.ocr.url}")
    private String url;


    public static void main(String[] args) {
        //这里进行 list的 测试
        List<User> list = getList(20);

        List<List<User>>  result = new ArrayList<>();

        List<User> tempList = new ArrayList<>();

        for (User user : list) {
            if(CollectionUtils.isEmpty(tempList)){
                tempList.add(user);
            }else {
                //如果不是空的   那么就进行age的判断  如果不能被三整除  就加入到  tempList
                if (Objects.equals(user.getAge() % 3,0)) {
                    //如果相等的话
                    tempList.add(user);
                    //这里就是深复制一下    然后进行存储
                    List<User> temp = new ArrayList<>();
                    CollectionUtils.addAll(temp,new Object[tempList.size()]);
                    Collections.copy(temp,tempList);
                    result.add(temp);
                    tempList.clear();
                }else {
                    tempList.add(user);
                }
            }
        }
        for (int i = 0; i < result.size(); i++) {
            List<User> users = result.get(i);
            for (User user : users) {
                System.out.println(i+"========="+user.toString());
            }
        }


    }

    public static List<User>  getList(int num){
        ChineseNameGenerator instance = ChineseNameGenerator.getInstance();
        GenericGenerator generator = ChineseAddressGenerator.getInstance();
        List<User>  list = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            list.add(new User(i,instance.generate(),generator.generate(),Double.valueOf(i)));
        }
        return list;
    }
}
@Data
@AllArgsConstructor
@NoArgsConstructor
class User{
    private Integer age;
    private String name;
    private String adress;
    private Double money;
}
