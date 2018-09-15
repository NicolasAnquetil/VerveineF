package fr.inria.verveine.extractor.fortran.ast;

public class ASTModuleStmtNode extends ASTNode {
	public static final int TMODULE = 1;
	public static final int TEOS = 3;

    ASTToken label; // in ASTModuleStmtNode
    ASTToken hiddenTModule; // in ASTModuleStmtNode
    ASTModuleNameNode moduleName; // in ASTModuleStmtNode
    ASTToken hiddenTEos; // in ASTModuleStmtNode

    public ASTToken getLabel()
    {
        return this.label;
    }

    public void setLabel(ASTToken newValue)
    {
        this.label = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    public ASTModuleNameNode getModuleName()
    {
        return this.moduleName;
    }

    public void setModuleName(ASTModuleNameNode newValue)
    {
        this.moduleName = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    @Override
    public void accept(IASTVisitor visitor)
    {
        visitor.visitASTModuleStmtNode(this);
        visitor.visitASTNode(this);
    }

    @Override protected int getNumASTFields()
    {
        return 4;
    }

    @Override protected IASTNode getASTField(int index)
    {
        switch (index)
        {
        case 0:  return this.label;
        case TMODULE:  return this.hiddenTModule;
        case 2:  return this.moduleName;
        case TEOS:  return this.hiddenTEos;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }

    @Override protected void setASTField(int index, IASTNode value)
    {
        switch (index)
        {
        case 0:  this.label = (ASTToken)value; if (value != null) value.setParent(this); return;
        case TMODULE:  this.hiddenTModule = (ASTToken)value; if (value != null) value.setParent(this); return;
        case 2:  this.moduleName = (ASTModuleNameNode)value; if (value != null) value.setParent(this); return;
        case TEOS:  this.hiddenTEos = (ASTToken)value; if (value != null) value.setParent(this); return;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }

}
