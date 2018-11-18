const Discord = require("discord.js");
const client = new Discord.Client();

client.on('ready', () => {
  console.log(`Logged in as ${client.user.tag}!`);
});

client.on('message', msg => {
  if (msg.content === 'ping') {
    msg.reply('Pong!');
  }
});

client.login('NTEyNzUyNzg2OTI0ODk2MjU5.DtLsxA.5nvnvhJy67RZtN4D8JRFtad3y0w');