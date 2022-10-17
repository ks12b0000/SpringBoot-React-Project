import React from "react";
import { Container } from 'react-bootstrap';
import { Route } from "react-router-dom";
import Header from "./components/Header";

function App() {
  return (
     <div>
      <Header />
      <Container>
        <Route path="/" exact component={""} />
        <Route path="/saveForm" exact={true} component={""} />
        <Route path="/book/:id" exact={true} component={""} />
        <Route path="/loginForm" exact={true} component={""} />
        <Route path="/joinForm" exact={true} component={""} />
        <Route path="/updateForm/:id" exact={true} component={""} />
      </Container>
     </div>
  );
}

export default App;
