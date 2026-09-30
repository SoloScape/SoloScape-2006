import com.rs2.cache.js5.*; import java.io.*;
public class MusicAliases {public static void main(String[] args)throws Exception{try(Js5CacheStore s=new Js5CacheStore(new File("cache"))){Js5ReferenceTable t=s.readReferenceTable(6);for(String name:args)System.out.println(name+"="+t.getGroupId(name));}}}
