package wei.li.util

import scala.io.Source
import scala.collection.mutable

object DatabaseUtil {
    def sqlStruct2ListMap(sqlFile: String): List[Map[String,String]] = {
        val lines = Source.fromFile(sqlFile).getLines.toList.map(_.split("\t").toList)
        val head = lines.take(1).flatten
        val rows = lines.drop(1)
        for (row <- rows)
            yield (head zip row).toMap
    }
}


object GeneralUtil {
    def mapReplaceByKey(src: List[Map[String,String]], repSrc: Map[String,String], key: String): List[Map[String,String]] =  {
        def oneMapReplace(oneMap: mutable.Map[String,String]): Map[String,String] = {
            oneMap(key) = repSrc.get(oneMap(key)) match {
              case Some(value) => value
              case None => println(oneMap + "not replace"); oneMap(key)
            }
            Map.empty ++ oneMap
        }
        src.map(x =>oneMapReplace(mutable.Map.empty ++ x))
    }

    def enumCodeDesc2Map(enumString: String): (Map[String,String],Map[String,String]) = {
        enumString.split("""\)[,;]""").map{x => x.dropWhile(_ != '(').drop(1).split(",") match {
          case Array(code, desc) => (code -> splitAll(desc,"\" "), splitAll(desc, "\" ") -> code)
        }}.foldLeft((Map[String,String](),Map[String,String]())){(acc, ele) =>
            acc match {
              case (map1, map2) => (map1 + ele._1, map2 + ele._2)
            }
        }
    }

    def enumCodeName2Map(enumString: String): (Map[String,String],Map[String,String]) = {
        enumString.split("""\)[,;]""").map{x => x.split("""\(""") match {
          case Array(name, code) => (code.takeWhile(_ != ',') -> splitAll(name, "\" \n"), splitAll(name, "\" \n") -> code.takeWhile(_ != ','))
        }}.foldLeft((Map[String,String](),Map[String,String]())){(acc, ele) =>
            acc match {
              case (map1, map2) => (map1 + ele._1, map2 + ele._2)
            }
        }
    }

    def splitAll(str: String, bad: String): String = {
        str.dropWhile(bad.contains(_)).reverse.dropWhile(bad.contains(_)).reverse
    }
}
        
