module SkeletonPermissiveScript.main {

    requires BotWithUs.xapi;
    requires BotWithUs.api;
    requires com.google.gson;
    requires BotWithUs.imgui;

    provides net.botwithus.scripts.Script with net.example.scripts.ExampleScript;
}