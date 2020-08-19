const express = require('express');
const app = express();
var router = express.Router();
//const bodyParser = require('body-parser');
const https = require('https');
//const url = require('url');
const googleTrends = require('google-trends-api');
const port = process.env.PORT ||8000;

//var EventEmitter = require("events").EventEmitter;
//var request = require('request');
//'use strict';

//app.use(express.static(__dirname));
//app.use(bodyParser.json());
//app.use(bodyParser.urlencoded({extended: false}));

router.get('/guardianhome', (req, res) => {
  res.setHeader('Content-Type', "application/json");
  res.header("Access-Control-Allow-Origin", "*");
  res.header("Access-Control-Allow-Headers", "X-Requested-With");
  //var bd = new EventEmitter();
  // console.log(req.query);
  //var token = req.query.token;
  // console.log(token);
  var url = "https://content.guardianapis.com/search?api-key=e2ba87e1-98af-4096-a9db-c5c8829df651&section=(sport|business|technology|politics)&show-blocks=all";
  https.get(url, resp => {
    resp.setEncoding("utf8");
    let data = '';
  // A chunk of data has been recieved.
    resp.on('data', (chunk) => {
      data += chunk;
    });
  // The whole response has been received. Print out the result.
    resp.on('end', () => {
      res.send(JSON.parse(data));
      //console.log(JSON.parse(data));
    });

    }).on("error", (err) => {
    console.log("Error: " + err.message);
    });
})

router.get('/trendingapi', (req,res) =>{
  var que = req.query;
  var key = que.keyword;
  googleTrends.interestOverTime({keyword: key, startTime: new Date('2019-06-01')})
  .then(function(results){
    res.send(JSON.parse(results));
    //console.log(results);
  })
  .catch(function(err){
    console.error(err);
  });
});

router.get('/guardianhomecall', (req, res) => {
  res.setHeader('Content-Type', "application/json");
  res.header("Access-Control-Allow-Origin", "*");
  res.header("Access-Control-Allow-Headers", "X-Requested-With");
  //var bd = new EventEmitter();
  // console.log(req.query);
  //var token = req.query.token;
  // console.log(token);
  var url = "https://content.guardianapis.com/search?order-by=newest&show-fields=starRating,headline,thumbnail,short-url&api-key=e2ba87e1-98af-4096-a9db-c5c8829df651";
  https.get(url, resp => {
    resp.setEncoding("utf8");
    let data = '';
  // A chunk of data has been recieved.
    resp.on('data', (chunk) => {
      data += chunk;
    });
  // The whole response has been received. Print out the result.
    resp.on('end', () => {
      res.send(JSON.parse(data));
      //console.log(JSON.parse(data));
    });

    }).on("error", (err) => {
    console.log("Error: " + err.message);
    });
})

router.get('/guardiansection', (req, res) => {
  res.setHeader('Content-Type', "application/json");
  res.header("Access-Control-Allow-Origin", "*");
  res.header("Access-Control-Allow-Headers", "X-Requested-With");
  //var bd = new EventEmitter();
  var que = req.query;
  var section = que.section;
  // console.log(req.query);
  // console.log(token);
  var url = "https://content.guardianapis.com/search?section=" + section + "&api-key=e2ba87e1-98af-4096-a9db-c5c8829df651&show-blocks=all";
  //console.log("URL", url);
  https.get(url, resp => {
    resp.setEncoding("utf8");
    let data = '';
  // A chunk of data has been recieved.
    resp.on('data', (chunk) => {
    data += chunk;
    });
  // The whole response has been received. Print out the result.
    resp.on('end', () => {
    res.send(JSON.parse(data));
    //console.log(JSON.parse(data).explanation);
    });

    }).on("error", (err) => {
    console.log("Error: " + err.message);
    });
})

router.get('/guardiansearch', (req, res) => {
  res.setHeader('Content-Type', "application/json");
  res.header("Access-Control-Allow-Origin", "*");
  res.header("Access-Control-Allow-Headers", "X-Requested-With");
  //var bd = new EventEmitter();
  var que = req.query;
  var keyword = que.q;
  // console.log(req.query);
  // console.log(token);
  var url = "https://content.guardianapis.com/search?q=" + keyword + "&api-key=e2ba87e1-98af-4096-a9db-c5c8829df651&show-blocks=all";
  //console.log("URL", url)
  https.get(url, resp => {
    resp.setEncoding("utf8");
    let data = '';
  // A chunk of data has been recieved.
    resp.on('data', (chunk) => {
    data += chunk;
    });
  // The whole response has been received. Print out the result.
    resp.on('end', () => {
    res.send(JSON.parse(data));
    console.log(JSON.parse(data).explanation);
    });

    }).on("error", (err) => {
    console.log("Error: " + err.message);
    });
})

router.get('/guardiandetailed', (req, res) => {
  res.setHeader('Content-Type', "application/json");
  res.header("Access-Control-Allow-Origin", "*");
  res.header("Access-Control-Allow-Headers", "X-Requested-With");
  //var bd = new EventEmitter();
  var que = req.query;
  var article_id = que.id;
  // console.log(req.query);
  // console.log(token);
  //console.log(req);
  var url = "https://content.guardianapis.com/" + article_id + "?api-key=e2ba87e1-98af-4096-a9db-c5c8829df651&show-blocks=all";
  https.get(url, resp => {
    resp.setEncoding("utf8");
    let data = '';
  // A chunk of data has been recieved.
    resp.on('data', (chunk) => {
    data += chunk;
    });
  // The whole response has been received. Print out the result.
    resp.on('end', () => {
    res.send(JSON.parse(data));
    console.log(JSON.parse(data).explanation);
    });

    }).on("error", (err) => {
    console.log("Error: " + err.message);
    });
})

router.get('/nythome', (req, res) => {
  res.setHeader('Content-Type', "application/json");
  res.header("Access-Control-Allow-Origin", "*");
  res.header("Access-Control-Allow-Headers", "X-Requested-With");
  //var bd = new EventEmitter();
  // console.log(req.query);
  var token = req.query.token;
  // console.log(token);
  var url = "https://api.nytimes.com/svc/topstories/v2/home.json?api-key=2KjGiGQyGMI1wYeNL2kGh8aRDXxPIamN";
  https.get(url, resp => {
    resp.setEncoding("utf8");
    let data = '';
  // A chunk of data has been recieved.
    resp.on('data', (chunk) => {
    data += chunk;
    });
  // The whole response has been received. Print out the result.
    resp.on('end', () => {
    res.send(JSON.parse(data));
    console.log(JSON.parse(data).explanation);
    });

    }).on("error", (err) => {
    console.log("Error: " + err.message);
    });
})

router.get('/nytsection', (req, res) => {
  res.setHeader('Content-Type', "application/json");
  res.header("Access-Control-Allow-Origin", "*");
  res.header("Access-Control-Allow-Headers", "X-Requested-With");
  //var bd = new EventEmitter();
  var que = req.query;
  var section = que.section;
  // console.log(req.query);
  // console.log(token);
  var url = "https://api.nytimes.com/svc/topstories/v2/" + section + ".json?api-key=2KjGiGQyGMI1wYeNL2kGh8aRDXxPIamN";
  //console.log("URL", url);
  https.get(url, resp => {
    resp.setEncoding("utf8");
    let data = '';
  // A chunk of data has been recieved.
    resp.on('data', (chunk) => {
    data += chunk;
    });
  // The whole response has been received. Print out the result.
    resp.on('end', () => {
    res.send(JSON.parse(data));
    console.log(JSON.parse(data).explanation);
    });

    }).on("error", (err) => {
    console.log("Error: " + err.message);
    });
})

router.get('/nytsearch', (req, res) => {
  res.setHeader('Content-Type', "application/json");
  res.header("Access-Control-Allow-Origin", "*");
  res.header("Access-Control-Allow-Headers", "X-Requested-With");
  //var bd = new EventEmitter();
  var que = req.query;
  var keyword = que.q;
  // console.log(req.query);
  // console.log(token);
  var url = "https://api.nytimes.com/svc/search/v2/articlesearch.json?q=" + keyword + "&api-key=2KjGiGQyGMI1wYeNL2kGh8aRDXxPIamN";
  //console.log("URL", url);
  https.get(url, resp => {
    resp.setEncoding("utf8");
    let data = '';
  // A chunk of data has been recieved.
    resp.on('data', (chunk) => {
    data += chunk;
    });
  // The whole response has been received. Print out the result.
    resp.on('end', () => {
    res.send(JSON.parse(data));
    console.log(JSON.parse(data).explanation);
    });

    }).on("error", (err) => {
    console.log("Error: " + err.message);
    });
})

router.get('/nytdetailed', (req, res) => {
  res.setHeader('Content-Type', "application/json");
  res.header("Access-Control-Allow-Origin", "*");
  res.header("Access-Control-Allow-Headers", "X-Requested-With");
  //var bd = new EventEmitter();
  var que = req.query;
  var article_web_url = que.id;
  // console.log(req.query);
  // console.log(token);
  var url = "https://api.nytimes.com/svc/search/v2/articlesearch.json?fq=web_url:(%22" + article_web_url + "%22)&api-key=2KjGiGQyGMI1wYeNL2kGh8aRDXxPIamN";
  //console.log(url);
  https.get(url, resp => {
    resp.setEncoding("utf8");
    let data = '';
  // A chunk of data has been recieved.
    resp.on('data', (chunk) => {
    data += chunk;
    });
  // The whole response has been received. Print out the result.
    resp.on('end', () => {
    res.send(JSON.parse(data));
    console.log(JSON.parse(data).explanation);
    });

    }).on("error", (err) => {
    console.log("Error: " + err.message);
    });
})

app.use(router);

app.listen(port, function(){
	console.log("server started on port " + port);
});

module.exports = app;