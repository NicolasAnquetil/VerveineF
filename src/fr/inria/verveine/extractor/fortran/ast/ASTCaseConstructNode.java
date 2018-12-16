package fr.inria.verveine.extractor.fortran.ast;

public class ASTCaseConstructNode extends ASTNode {
	
	/*ASTSelectCaseStmtNode selectCaseStmt; // in ASTCaseConstructNode
    IASTListNode<ICaseBodyConstruct> selectCaseBody; // in ASTCaseConstructNode
    ASTEndSelectStmtNode endSelectStmt; // in ASTCaseConstructNode

    public ASTSelectCaseStmtNode getSelectCaseStmt()
    {
        return this.selectCaseStmt;
    }

    public void setSelectCaseStmt(ASTSelectCaseStmtNode newValue)
    {
        this.selectCaseStmt = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    public IASTListNode<ICaseBodyConstruct> getSelectCaseBody()
    {
        return this.selectCaseBody;
    }

    public void setSelectCaseBody(IASTListNode<ICaseBodyConstruct> newValue)
    {
        this.selectCaseBody = newValue;
        if (newValue != null) newValue.setParent(this);
    }


    public ASTEndSelectStmtNode getEndSelectStmt()
    {
        return this.endSelectStmt;
    }

    public void setEndSelectStmt(ASTEndSelectStmtNode newValue)
    {
        this.endSelectStmt = newValue;
        if (newValue != null) newValue.setParent(this);
    }
*/

	@Override
    public void accept(IASTVisitor visitor)
    {
        visitor.visitASTCaseConstructNode(this);
//        visitor.visitIExecutableConstruct(this);
//        visitor.visitASTNode(this);
    }

    @Override protected int getNumASTFields()
    {
        return 3;
    }

    @Override protected IASTNode getASTField(int index)
    {
        switch (index)
        {
        case 0:  return new ASTNullNode(); // this.selectCaseStmt;
        case 1:  return new ASTNullNode(); // this.selectCaseBody;
        case 2:  return new ASTNullNode(); // this.endSelectStmt;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }

    @Override protected void setASTField(int index, IASTNode value)
    {
        switch (index)
        {
        case 0: return; // this.selectCaseStmt = (ASTSelectCaseStmtNode)value; if (value != null) value.setParent(this); return;
        case 1: return; // this.selectCaseBody = (IASTListNode<ICaseBodyConstruct>)value; if (value != null) value.setParent(this); return;
        case 2: return; // this.endSelectStmt = (ASTEndSelectStmtNode)value; if (value != null) value.setParent(this); return;
        default: throw new IllegalArgumentException("Invalid index");
        }
    }
}
