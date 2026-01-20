package com.craftinginterpreters.lox_s;

import java.util.List;

class AstPrinter implements Expr.Visitor<String>, Stmt.Visitor<String> {

  String print(Stmt stmt) {
    return stmt.accept(this);
  }

  @Override
  public String visitExpressionStmt(Stmt.Expression stmt) {
    return parenthesize(";", stmt.expression);
  }

  @Override
  public String visitPrintStmt(Stmt.Print stmt) {
    return parenthesize("print", stmt.expression);
  }

  @Override
  public String visitVarStmt(Stmt.Var stmt) {
    if (stmt.initializer == null) {
      return "(var " + stmt.name.lexeme + ")";
    }
    return "(var " + stmt.name.lexeme + " = " +
        stmt.initializer.accept(this) + ")";
  }

  @Override
  public String visitAssignExpr(Expr.Assign expr) {
    return "(= " + expr.name.lexeme + " " +
        expr.value.accept(this) + ")";
  }

  @Override
  public String visitBinaryExpr(Expr.Binary expr) {
    return parenthesize(expr.operator.lexeme,
        expr.left, expr.right);
  }

  @Override
  public String visitGroupingExpr(Expr.Grouping expr) {
    return parenthesize("group", expr.expression);
  }

  @Override
  public String visitLiteralExpr(Expr.Literal expr) {
    if (expr.value == null)
      return "nil";
    return expr.value.toString();
  }

  @Override
  public String visitUnaryExpr(Expr.Unary expr) {
    return parenthesize(expr.operator.lexeme, expr.right);
  }

  @Override
  public String visitVariableExpr(Expr.Variable expr) {
    return expr.name.lexeme;
  }

  private String parenthesize(String name, Expr... exprs) {
    StringBuilder builder = new StringBuilder();

    builder.append("(").append(name);
    for (Expr expr : exprs) {
      builder.append(" ");
      builder.append(expr.accept(this));
    }
    builder.append(")");

    return builder.toString();
  }
}
