Nmap所识别的6个端口状态：

- `open`(开放的) 

应用程序正在该端口接收TCP 连接或者UDP报文。发现这一点常常是端口扫描的主要目标。安全意识强的人们知道每个开放的端口都是攻击的入口。攻击者或者入侵测试者想要发现开放的端口。而管理员则试图关闭它们或者用防火墙保护它们以免妨碍了合法用户。非安全扫描可能对开放的端口也感兴趣，因为它们显示了网络上那些服务可供使用。 

- `closed`(关闭的) 

关闭的端口对于Nmap也是可访问的(它接受Nmap的探测报文并作出响应)，但没有应用程序在其上监听。它们可以显示该IP地址上(主机发现，或者ping扫描)的主机正在运行up也对部分操作系统探测有所帮助。因为关闭的关口是可访问的，也许过会儿值得再扫描一下，可能一些又开放了。系统管理员可能会考虑用防火墙封锁这样的端口。那样他们就会被显示为被过滤的状态，下面讨论。 

- `filtered`(被过滤的) 

由于包过滤阻止探测报文到达端口，Nmap无法确定该端口是否开放。过滤可能来自专业的防火墙设备，路由器规则或者主机上的软件防火墙。这样的端口让攻击者感觉很挫折，因为它们几乎不提供任何信息。有时候它们响应ICMP错误消息如类型3代码13(无法到达目标:通信被管理员禁止)，但更普遍的是过滤器只是丢弃探测帧，不做任何响应。这迫使Nmap重试若干次以防万一探测包是由于网络阻塞丢弃的。这使得扫描速度明显变慢。 

- `unfiltered`(未被过滤的) 

未被过滤状态意味着端口可访问，但Nmap不能确定它是开放还是关闭。只有用于映射防火墙规则集的ACK扫描才会把端口分类到这种状态。用其它类型的扫描如窗口扫描，SYN扫描，或者FIN扫描来扫描未被过滤的端口可以帮助确定端口是否开放。 

- `open|filtered`(开放或者被过滤的) 

当无法确定端口是开放还是被过滤的，Namp就把该端口划分成这种状态。开放的端口不响应就是一个例子。没有响应也可能意味着报文过滤器丢弃了探测报文或者它引发的任何响应。因此Nmap无法确定该端口是开放的还是被过滤的。UDP，IP协议，FIN，Null，和Xmas扫描可能把端口归入此类。

- `closed|filtered`(关闭或者被过滤的) 

该状态用于Nmap不能确定端口是关闭的还是被过滤的。它只可能出现在IPID Idle扫描中。
