
[![LICENSE](https://img.shields.io/badge/license-Apache%202-blue)](https://github.com/paxoscn/code-graph/blob/master/LICENSE)
[![Language](https://img.shields.io/badge/Language-Java-green)](https://www.oracle.com/java/technologies/)

# Code Graph - 用于从项目代码生成依赖关系图的工具

## 介绍

当你面对一个陌生的项目想要阅读其代码时, 使用本工具可根据该项目中的代码快速生成一张依赖关系图, 来帮助你快速了解该项目的全貌.

以一个Java项目为例(你可以在./java/examples/example-1找到该项目), 其核心代码如下:

com/example/King.java

```java
public class King {

    private Chef chef;
    
    public void haveDinner() {
        // 主厨向国王提供晚餐.
        List<Food> dinner = chef.prepareDinner();
        
        // Eating dinner.
    }
}
```

com/example/Knight.java

```java
public class Knight {

    private Cook cook;
    
    public void haveDinner() {
        // 厨师向骑士提供晚餐.
        Food dinner = cook.cook();
        
        // Eating dinner.
    }
}
```

com/example/Chef.java

```java
import java.util.LinkedList;

public class Chef {

    private List<Cook> cooks;

    public List<Food> prepareDinner() {
        // 主厨指挥所有厨师一起制作晚餐.
        List<Food> dinner = new LinkedList<>();
        
        for (Cook cook : cooks) {
            Food food = cook.cook();
            
            dinner.add(food);
        }
        
        return dinner;
    }
}
```

com/example/Cook.java

```java
public class Cook {
    
    public Food cook() {
        // 厨师负责制作一份食物.
        
        return null;
    }
}
```

进入./java目录, 运行CodeGraph工具:

```shell
./mvnw compile exec:java -Dexec.args="./examples/example-1"
```

运行完成后, 将打印出一个PlantUML格式的调用关系图:

```puml
left to right direction
King ..> Chef
Knight ..> Cook
Chef ..> Cook
```

## 快速开始

### Java项目

进入./java目录, 执行:

```shell
./mvnw compile exec:java -Dexec.args="[你的Java项目路径]"
```

运行完成后, 将打印出一个PlantUML格式的调用关系图.

你可以在

[https://plantumlviewer.web.app/](https://plantumlviewer.web.app/)

上渲染它并导出图片.

你也可以用支持PlantUML格式的IDE插件或程序来渲染它.

## 开源协议

本项目使用Apache 2.0协议. 详情请见: [LICENSE](./LICENSE).

## 告知

- 联系方式 - 微信: 95634620 邮件: [unrealwalker@126.com](mailto:unrealwalker@126.com)
