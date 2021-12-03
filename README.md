# Gamermode

## Contributing

When contributing please make sure at the very least you have nodejs and yarn installed and have ran `yarn prepare` in
this repo to install the git hooks. This is because we try and keep all of the code pretty using prettier and intellij's
prettier plugin doesn't support java files even when added to the regex.

## Design choices

- No custom NBT implementation
    - In the first version of Gamermode, I wrote
      a [custom NBT implementation](https://github.com/Floffah/gamermode/tree/new/src/main/java/dev/floffah/gamermode/nbt)
      however it was unstable and too bloated. Instead, I opted to use something that people are more familiar with (
      Querz's implementation)
- No custom components implementation
    - This was mainly motivated by laziness and that I thought it might be better to use something more fleshed out,
      stable, and known in the community and ended up going with KyoriPowered's Adventure

## Helpful links for implementation

 - [Wiki.vg](https://wiki.vg/Main_Page)
 - [HexEd.it](https://www.hexed.it/)
 - [UUID hex to m&l long to int array](https://www.soltoder.com/mc-uuid-converter/)

## Implemented

A list of everything that has been implemented so far

### Packets

#### [Handshaking State](https://wiki.vg/Protocol#Handshaking)

- [x] C->S 0x00 Handshake - [class](./GamerModeServer/src/main/java/dev/floffah/gamermode/server/packet/serverlist/Handshake.java), [protocol wiki](https://wiki.vg/Protocol#Handshake)
- [ ] C->S 0xFE Legacy Server List Ping - ~~class~~, [protocol wiki](https://wiki.vg/Protocol#Legacy_Server_List_Ping)
    - not a priority

#### [Status State](https://wiki.vg/Protocol#Status)
- [x] S->C 0x00 Response - [class](./GamerModeServer/src/main/java/dev/floffah/gamermode/server/packet/serverlist/Response.java), [protocol wiki](https://wiki.vg/Protocol#Response)
- [x] C->S 0x00 Request - [class](./GamerModeServer/src/main/java/dev/floffah/gamermode/server/packet/serverlist/Request.java), [protocol wiki](https://wiki.vg/Protocol#Request)
- [x] S->C 0x01 Pong - [class](./GamerModeServer/src/main/java/dev/floffah/gamermode/server/packet/serverlist/Pong.java), [protocol wiki](https://wiki.vg/Protocol#Pong)
- [x] C->S 0x01 Ping - [class](./GamerModeServer/src/main/java/dev/floffah/gamermode/server/packet/serverlist/Ping.java), [protocol wiki](https://wiki.vg/Protocol#Ping)

#### [Login State](https://wiki.vg/Protocol#Login)
- [x] S->C 0x00 Disconnect - [class](./GamerModeServer/src/main/java/dev/floffah/gamermode/server/packet/connection/LoginDisconnect.java), [protocol wiki](https://wiki.vg/Protocol#Disconnect_.28login.29)
- [x] C->S 0x00 Login Start - [class](./GamerModeServer/src/main/java/dev/floffah/gamermode/server/packet/login/LoginStart.java), [protocol wiki](https://wiki.vg/Protocol#Login_Start)
- [x] S->C 0x01 Encryption Request - [class](./GamerModeServer/src/main/java/dev/floffah/gamermode/server/packet/login/EncryptionRequest.java), [protocol wiki](https://wiki.vg/Protocol#Encryption_Request)
- [x] C->S 0x01 Encryption Response - [class](./GamerModeServer/src/main/java/dev/floffah/gamermode/server/packet/login/EncryptionResponse.java), [protocol wiki](https://wiki.vg/Protocol#Encryption_Response)
- [x] S->C 0x02 Login Success - [class](./GamerModeServer/src/main/java/dev/floffah/gamermode/server/packet/login/LoginSuccess.java), [protocol wiki](https://wiki.vg/Protocol#Login_Success)
- [ ] C->S 0x02 Login Plugin Response - ~~class~~, [protocol wiki](https://wiki.vg/Protocol#Login_Plugin_Response)
    - will not implement
- [ ] S->C 0x03 Set Compression - ~~class~~, [protocol wiki](https://wiki.vg/Protocol#Set_Compression)
    - not a priority (yet)
- [ ] C->S 0x03 Login Plugin Request - ~~class~~, [protocol wiki](https://wiki.vg/Protocol#Login_Plugin_Request)
    - will not implement

#### [Play State](https://wiki.vg/Protocol#Play)
- [x] C->S 0x0A Plugin Message - [class](./GamerModeServer/src/main/java/dev/floffah/gamermode/server/packet/play/message/PluginMessage.java), [protocol wiki](https://wiki.vg/Protocol#Plugin_Message_.28serverbound.29)
- [x] S->C 0x0E Server Difficulty - [class](./GamerModeServer/src/main/java/dev/floffah/gamermode/server/packet/play/info/ServerDifficulty.java), [protocol wiki](https://wiki.vg/Protocol#Server_Difficulty)
- [x] S->C 0x18 Plugin Message - [class](./GamerModeServer/src/main/java/dev/floffah/gamermode/server/packet/play/message/PluginMessage.java), [protocol wiki](https://wiki.vg/Protocol#Plugin_Message_.28clientbound.29)
- [x] S->C 0x1A Disconnect - [class](./GamerModeServer/src/main/java/dev/floffah/gamermode/server/packet/connection/Disconnect.java), [protocol wiki](https://wiki.vg/Protocol#Disconnect_.28play.29)
- [x] S->C 0x26 Join Game - [class](./GamerModeServer/src/main/java/dev/floffah/gamermode/server/packet/play/JoinGame.java), [protocol wiki](https://wiki.vg/Protocol#Join_Game)