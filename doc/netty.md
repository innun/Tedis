### Channel

生命周期：

![1563856290760](C:\Users\jing\AppData\Roaming\Typora\typora-user-images\1563856290760.png)




* 一个EventLoopGroup 包含一个或者多个EventLoop；
* 一个EventLoop 在它的生命周期内只和一个Thread 绑定；
* 所有由EventLoop 处理的I/O 事件都将在它专有的Thread 上被处理；
* 一个Channel 在它的生命周期内只注册于一个EventLoop；
* 一个EventLoop 可能会被分配给一个或多个Channel。



### ChannelHandlerContext 、 ChannelHandler 和 ChannelPipeline

* 三者关系

![1563863581887](C:\Users\jing\AppData\Roaming\Typora\typora-user-images\1563863581887.png)

* 事件的流动方向

![1563849256546](C:\Users\jing\AppData\Roaming\Typora\typora-user-images\1563849256546.png)



* ChannelHandlerContext   管理着ChannelHandler和ChannelPipeline之间的交互，它包含着与pipline和handler中一些功能一样的方法，只不过通过context调用时，数据会从当前handler开始流动，而在pipeline和handler上调用时，会在整个pipeline流动。

  ![1563863964723](C:\Users\jing\AppData\Roaming\Typora\typora-user-images\1563863964723.png)

  ![1563863981699](C:\Users\jing\AppData\Roaming\Typora\typora-user-images\1563863981699.png)

* 共享ChannelHandler

  使用@Sharable注解就可以让多个pipeline使用同一个handler，但是考虑到，多个channel的并发调用可能造成线程不安全的问题，所以不要共享的handler不应该存在修改状态的操作。

### ByteBuf

![1563851417879](C:\Users\jing\AppData\Roaming\Typora\typora-user-images\1563851417879.png)

* PooledByteBufAllocator

  底层由jemalloc实现



> 零拷贝
>
> 零拷贝（zero-copy）是一种目前只有在使用NIO 和Epoll 传输时才可使用的特性。它使你可以快速
> 高效地将数据从文件系统移动到网络接口，而不需要将其从内核空间复制到用户空间，其在像FTP 或者
> HTTP 这样的协议中可以显著地提升性能。但是，并不是所有的操作系统都支持这一特性。特别地，它对
> 于实现了数据加密或者压缩的文件系统是不可用的——只能传输文件的原始内容。反过来说，传输已被
> 加密的文件则不是问题。