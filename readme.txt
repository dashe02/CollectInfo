此项目为接受信息方,接受数据后写入文件,文件是以ip+当天的时间来标识的
然后通过hadoop集群每隔24小时来处理一下,得到每个服务器的状况系数

hadoop集群处理负责,文件的读取,通过马尔科夫链算法来处理,最后归并
传给hadoop集群的是一个List<Map<String,String>>
map为Map<服务器ip,系统信息>