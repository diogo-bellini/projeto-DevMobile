const jsonServer = require("json-server");
const server = jsonServer.create();
const router = jsonServer.router("db.json");
const middlewares = jsonServer.defaults();

server.use(middlewares);
server.use(jsonServer.bodyParser);

server.post("/login", (req, res) => {
  const { email, senha } = req.body;

  if (!email || !senha) {
    return res.status(400).json({ error: "Email e senha são obrigatórios." });
  }

  const db = router.db;
  const users = db.get("users").value();

  const user = users.find(
    (u) => u.email === email && u.senha === senha
  );

  if (!user) {
    return res.status(401).json({ error: "Email ou senha incorretos." });
  }

  const { senha: _, ...userWithoutPassword } = user;
  return res.status(200).json(userWithoutPassword);
});

server.use(router);

server.listen(3000, "0.0.0.0", () => {
  console.log("JSON Server rodando em http://0.0.0.0:3000");
});