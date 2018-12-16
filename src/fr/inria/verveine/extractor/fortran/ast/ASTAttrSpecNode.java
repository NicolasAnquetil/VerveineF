package fr.inria.verveine.extractor.fortran.ast;

public class ASTAttrSpecNode extends ASTNode {
	ASTToken isSave;
	ASTToken isPointer;
	ASTToken isValue;
	ASTToken isExternal;
	ASTToken isDimension;
	ASTToken isVolatile;
	ASTToken isAsync;
	ASTToken isContiguous;
	ASTToken isProtected;
	ASTToken isCodimension;
	ASTToken hiddenTLbracket;
	//ASTCoarraySpecNode coarraySpec;
	ASTToken hiddenTRbracket;
	ASTAccessSpecNode accessSpec;
	ASTToken isIntrinsic;
	//ASTLanguageBindingSpecNode languageBindingSpec;
	ASTToken isAllocatable;
	ASTToken isOptional;
	ASTToken isTarget;
	ASTToken isIntent;
	ASTToken hiddenTLparen;
	//ASTArraySpecNode arraySpec;
	//ASTIntentSpecNode intentSpec;
	ASTToken hiddenTRparen;
	ASTToken isParameter;

	public boolean isSave()
	{
		return this.isSave != null;
	}

	public void setIsSave(ASTToken newValue)
	{
		this.isSave = newValue;
		if (newValue != null) newValue.setParent(this);
	}


	public boolean isPointer()
	{
		return this.isPointer != null;
	}

	public void setIsPointer(ASTToken newValue)
	{
		this.isPointer = newValue;
		//if (newValue != null) newValue.setParent(this);
	}


	public boolean isValue()
	{
		return this.isValue != null;
	}

	public void setIsValue(ASTToken newValue)
	{
		this.isValue = newValue;
		if (newValue != null) newValue.setParent(this);
	}


	public boolean isExternal()
	{
		return this.isExternal != null;
	}

	public void setIsExternal(ASTToken newValue)
	{
		this.isExternal = newValue;
		if (newValue != null) newValue.setParent(this);
	}


	public boolean isDimension()
	{
		return this.isDimension != null;
	}

	public void setIsDimension(ASTToken newValue)
	{
		this.isDimension = newValue;
		if (newValue != null) newValue.setParent(this);
	}


	public boolean isVolatile()
	{
		return this.isVolatile != null;
	}

	public void setIsVolatile(ASTToken newValue)
	{
		this.isVolatile = newValue;
		if (newValue != null) newValue.setParent(this);
	}


	public boolean isAsync()
	{
		return this.isAsync != null;
	}

	public void setIsAsync(ASTToken newValue)
	{
		this.isAsync = newValue;
		if (newValue != null) newValue.setParent(this);
	}


	public boolean isContiguous()
	{
		return this.isContiguous != null;
	}

	public void setIsContiguous(ASTToken newValue)
	{
		this.isContiguous = newValue;
		if (newValue != null) newValue.setParent(this);
	}


	public boolean isProtected()
	{
		return this.isProtected != null;
	}

	public void setIsProtected(ASTToken newValue)
	{
		this.isProtected = newValue;
		if (newValue != null) newValue.setParent(this);
	}


	public boolean isCodimension()
	{
		return this.isCodimension != null;
	}

	public void setIsCodimension(ASTToken newValue)
	{
		this.isCodimension = newValue;
		if (newValue != null) newValue.setParent(this);
	}

	/*
	    public ASTCoarraySpecNode getCoarraySpec()
	    {
	        return this.coarraySpec;
	    }

	    public void setCoarraySpec(ASTCoarraySpecNode newValue)
	    {
	        this.coarraySpec = newValue;
	        if (newValue != null) newValue.setParent(this);
	    }
*/

	public ASTAccessSpecNode getAccessSpec()
	{
		return this.accessSpec;
	}

	public void setAccessSpec(ASTAccessSpecNode newValue)
	{
		this.accessSpec = newValue;
		if (newValue != null) newValue.setParent(this);
	}

	public boolean isIntrinsic()
	{
		return this.isIntrinsic != null;
	}

	public void setIsIntrinsic(ASTToken newValue)
	{
		this.isIntrinsic = newValue;
		if (newValue != null) newValue.setParent(this);
	}

	/*
	    public ASTLanguageBindingSpecNode getLanguageBindingSpec()
	    {
	        return this.languageBindingSpec;
	    }

	    public void setLanguageBindingSpec(ASTLanguageBindingSpecNode newValue)
	    {
	        this.languageBindingSpec = newValue;
	        if (newValue != null) newValue.setParent(this);
	    }
	 */

	public boolean isAllocatable()
	{
		return this.isAllocatable != null;
	}

	public void setIsAllocatable(ASTToken newValue)
	{
		this.isAllocatable = newValue;
		if (newValue != null) newValue.setParent(this);
	}


	public boolean isOptional()
	{
		return this.isOptional != null;
	}

	public void setIsOptional(ASTToken newValue)
	{
		this.isOptional = newValue;
		if (newValue != null) newValue.setParent(this);
	}


	public boolean isTarget()
	{
		return this.isTarget != null;
	}

	public void setIsTarget(ASTToken newValue)
	{
		this.isTarget = newValue;
		if (newValue != null) newValue.setParent(this);
	}


	public boolean isIntent()
	{
		return this.isIntent != null;
	}

	public void setIsIntent(ASTToken newValue)
	{
		this.isIntent = newValue;
		if (newValue != null) newValue.setParent(this);
	}

	/*
	    public ASTArraySpecNode getArraySpec()
	    {
	        return this.arraySpec;
	    }

	    public void setArraySpec(ASTArraySpecNode newValue)
	    {
	        this.arraySpec = newValue;
	        if (newValue != null) newValue.setParent(this);
	    }


	    public ASTIntentSpecNode getIntentSpec()
	    {
	        return this.intentSpec;
	    }

	    public void setIntentSpec(ASTIntentSpecNode newValue)
	    {
	        this.intentSpec = newValue;
	        if (newValue != null) newValue.setParent(this);
	    }
	 */

	public boolean isParameter()
	{
		return this.isParameter != null;
	}

	public void setIsParameter(ASTToken newValue)
	{
		this.isParameter = newValue;
		if (newValue != null) newValue.setParent(this);
	}

	@Override
	public void accept(IASTVisitor visitor)
	{
		visitor.visitASTAttrSpecNode(this);
//		visitor.visitASTNode(this);
	}

	@Override protected int getNumASTFields()
	{
		return 25;
	}

	    @Override protected IASTNode getASTField(int index)
	    {
	        switch (index)
	        {
	        case 0:  return this.isSave;
	        case 1:  return this.isPointer;
	        case 2:  return this.isValue;
	        case 3:  return this.isExternal;
	        case 4:  return this.isDimension;
	        case 5:  return this.isVolatile;
	        case 6:  return this.isAsync;
	        case 7:  return this.isContiguous;
	        case 8:  return this.isProtected;
	        case 9:  return this.isCodimension;
	        case 10: return this.hiddenTLbracket;
	        case 11: return new ASTNullNode(); //this.coarraySpec;
	        case 12: return this.hiddenTRbracket;
	        case 13: return this.accessSpec;
	        case 14: return this.isIntrinsic;
	        case 15: return new ASTNullNode(); //this.languageBindingSpec;
	        case 16: return this.isAllocatable;
	        case 17: return this.isOptional;
	        case 18: return this.isTarget;
	        case 19: return this.isIntent;
	        case 20: return this.hiddenTLparen;
	        case 21: return new ASTNullNode(); //this.arraySpec;
	        case 22: return new ASTNullNode(); //this.intentSpec;
	        case 23: return this.hiddenTRparen;
	        case 24: return this.isParameter;
	        default: throw new IllegalArgumentException("Invalid index");
	        }
	    }

	    @Override protected void setASTField(int index, IASTNode value)
	    {
	        switch (index)
	        {
	        case 0:  this.isSave = (ASTToken)value; if (value != null) value.setParent(this); return;
	        case 1:  this.isPointer = (ASTToken)value; if (value != null) value.setParent(this); return;
	        case 2:  this.isValue = (ASTToken)value; if (value != null) value.setParent(this); return;
	        case 3:  this.isExternal = (ASTToken)value; if (value != null) value.setParent(this); return;
	        case 4:  this.isDimension = (ASTToken)value; if (value != null) value.setParent(this); return;
	        case 5:  this.isVolatile = (ASTToken)value; if (value != null) value.setParent(this); return;
	        case 6:  this.isAsync = (ASTToken)value; if (value != null) value.setParent(this); return;
	        case 7:  this.isContiguous = (ASTToken)value; if (value != null) value.setParent(this); return;
	        case 8:  this.isProtected = (ASTToken)value; if (value != null) value.setParent(this); return;
	        case 9:  this.isCodimension = (ASTToken)value; if (value != null) value.setParent(this); return;
	        case 10: this.hiddenTLbracket = (ASTToken)value; if (value != null) value.setParent(this); return;
	        case 11: return; //this.coarraySpec = (ASTCoarraySpecNode)value; if (value != null) value.setParent(this); return;
	        case 12: this.hiddenTRbracket = (ASTToken)value; if (value != null) value.setParent(this); return;
	        case 13: this.accessSpec = (ASTAccessSpecNode)value; if (value != null) value.setParent(this); return;
	        case 14: this.isIntrinsic = (ASTToken)value; if (value != null) value.setParent(this); return;
	        case 15: return; //this.languageBindingSpec = (ASTLanguageBindingSpecNode)value; if (value != null) value.setParent(this); return;
	        case 16: this.isAllocatable = (ASTToken)value; if (value != null) value.setParent(this); return;
	        case 17: this.isOptional = (ASTToken)value; if (value != null) value.setParent(this); return;
	        case 18: this.isTarget = (ASTToken)value; if (value != null) value.setParent(this); return;
	        case 19: this.isIntent = (ASTToken)value; if (value != null) value.setParent(this); return;
	        case 20: this.hiddenTLparen = (ASTToken)value; if (value != null) value.setParent(this); return;
	        case 21: return; //this.arraySpec = (ASTArraySpecNode)value; if (value != null) value.setParent(this); return;
	        case 22: return; //this.intentSpec = (ASTIntentSpecNode)value; if (value != null) value.setParent(this); return;
	        case 23: this.hiddenTRparen = (ASTToken)value; if (value != null) value.setParent(this); return;
	        case 24: this.isParameter = (ASTToken)value; if (value != null) value.setParent(this); return;
	        default: throw new IllegalArgumentException("Invalid index");
	        }
	    }

	    @Override
	    public String toString() {
	    	// for debug purposes
	    	return "ASTAttrSpecNode";
	    }

}