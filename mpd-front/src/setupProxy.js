const proxy = require("http-proxy-middleware")

module.exports = app => {
    app.use(proxy("/stomp", {target: "http://localhost:8080", ws: true}))
    app.use(proxy("/webjars", {target: "http://localhost:8080"}))
};