# Referance

###  RESP (REdis Serialization Protocol) 规范

[RESP protocol](https://redis.io/topics/protocol#request-response-model)

#### 1.1 RESP中服务端响应的数据类型

1. **Simple Strings** 第一个字节是"+"
2. **Errors**第一个字节是"-"
3. **Integer**第一个字节是":"
4. **Bulk Strings**第一个字节是"$"
5. **Arrays**第一个字节是"*"

不同的部分都是以"\r\n"(CRLF)作为结束符

- **Simples Strings**

  格式："+" + str + CRLF

  示例：+OK\r\n

- **Errors**

  客户端收到错误类型回复时应该抛出异常

  格式："-" + **Error Prefix** + " " + error msg  + CRLF

  示例：-WRONGTYPE Operation against a key holding the wrong kind of value\r\n

- **Integer**

  格式：":" +  int + CRLF

  示例：:1000\r\n"

- **Bulk Strings**

  格式："$" + len + CRLF + str + CRLF

  示例：$6\r\nfoobar\r\n 

  空串：$0\r\n\r\n

  NULL：$-1\r\n

- **Arrays**

  格式："*" + count + CRLF + [elms ...]

  示例：

  - 包含两个BulkString的数组： *2\r\n$3\r\nfoo\r\n$3\r\nbar\r\n

  - 包含三个Integer的数组： *3\r\n:1\r\n:2\r\n:3\r\n

  - NULL：*-1\r\n 

  - 包含NULL的数组：

    ["foo", nil, "bar"]

    ```
    *3\r\n
    $3\r\n
    foo\r\n
    $-1\r\n
    $3\r\n
    bar\r\n
    ```

#### 1.2 CS交互规范

- 客户端以 **Bulk Strings组成的Array**  为格式想服务端发送命令

- 服务端以 **数据类型** 中的其中一种作为响应

  示例：

  服务端向客户端发送命令： **LLEN mylist**，服务端返回48293，RESP协议交换如下：

  ```
  C => S *2$4\r\nLLEN\r\n$6\r\nmylist\r\n
  S => C :48293\r\n
  ```

#### 1.3 特例 （TODO）：

1. 支持管道，多条命令可以通过一次写一起发送，然后等待响应

   [管道](https://redis.io/topics/pipelining) 

2. 支持订阅， 服务端主动推送

