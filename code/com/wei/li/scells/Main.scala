package com.wei.li.scells
import swing._

object Main extends SimpleSwingApplication {
    def top = new MainFrame {
        title = "spreadshell"
        contents = new Spreadshell(10,10)
    }
}