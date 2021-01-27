package app.config

import app.config.config.AppConfig
import org.h2.tools.Server

var contacts = arrayListOf<Contact>()

fun main(){

    Server.createWebServer().start()
    AppConfig().setup().start()

}

fun initContacts(){
    contacts.add(Contact("Jhonathan", "36414872"))
    contacts.add(Contact("Suzie", "36414872"))
    contacts.add(Contact("Robert", "36414872"))
    contacts.add(Contact("Sally", "36414872"))
    contacts.add(Contact("Albert", "36414872"))
}