Formal Grammar Specification For BLZ
======

### Special identifiers

|Name|Meaning|
|-----|---------|
|NEWLINE|The newline character|
|IDENT|A variable identifier|
|NUM|A string of numerical characters|
|STR|A string of characters excluding a nonescaped "|


Definitions
=====

`Env := { IDENT }`

```
ValueList := Value , ValueList |
              Value
```

```
Value := IDENT |
         NUM |
         NUM.NUM |
         "STR" |
         Env |
         [ ValueList ]
```        

```
Line := Expression NEWLINE |
        If NEWLINE |
        While NEWLINE |
        For NEWLINE |
        Constructor NEWLINE |
        FunctionDecl NEWLINE |
        Import NEWLINE
```

```
Import := import IDENT |
          require IDENT
```

```
ParamList := IDENT , ParamList |
             IDENT
```

```
FunctionDecl := : IDENT |
                : IDENT ( ) |
                : IDENT ( ParamList )
```

`End := end`

`If := if Expression`

`While := while Expression`

`For := for Expression ; Expression ; Expression`

```
Constructor := constructor IDENT |
               constructor IDENT ( ) |
               constructor IDENT ( ParamList )
```

```
BinaryOp := + |
            - |
            / |
            * |
            % |
            = |
            ** |
            __ |
            == |
            != |
            ~= |
            < |
            > |
            <= |
            >= |
            && |
            ||
```

`BinaryExpression := Expression BinaryOp Expression`

```
UnaryOp := ++ |
           -- |
           !  |
           ( ValueList ) |
           [ Expression ]
```

`UnaryExpression := Expression UnaryOp`

`LambdaExpression := ( ParamList ) -> Expression`

```
Expression := BinaryExpression | 
              UnaryExpression |
              ( LambdaExpression ) |
              Value
```
