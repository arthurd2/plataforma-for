package org.forrisco.core.item;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.forpdi.core.abstractions.AbstractController;
import org.forpdi.core.company.CompanyDomain;
import org.forpdi.core.event.Current;
import org.forpdi.core.user.authz.AccessLevels;
import org.forpdi.core.user.authz.Permissioned;
import org.forrisco.core.item.Item;
import org.forrisco.core.policy.Policy;
import org.forrisco.core.policy.permissions.ManagePolicyPermission;

import br.com.caelum.vraptor.Consumes;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Delete;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.boilerplate.NoCache;
import br.com.caelum.vraptor.boilerplate.bean.PaginatedList;
import br.com.caelum.vraptor.boilerplate.util.GeneralUtils;

/**
 * @author Matheus Nascimento
 */
@Controller
public class ItemController extends AbstractController {
	
	@Inject @Current private CompanyDomain domain;
	@Inject private ItemBS bs;
	
	protected static final String PATH =  BASEPATH +"/item";
	
	/**
	 * Salvar Primeiro item
	 * 
	 * @return void
	 */
	@Post( PATH + "/info")
	@Consumes
	@NoCache
	@Permissioned(value = AccessLevels.COMPANY_ADMIN, permissions = { ManagePolicyPermission.class })
	public void saveInfo(@NotNull @Valid  Policy policy){
		
		try {
			
			Item item = new Item();
			item.setId(null);
			item.setPolicy(policy);
			item.setDescription(policy.getDescription());
			item.setName("Informações gerais");
			item.setFieldItem(null);
			this.bs.save(item);
			this.success(item);
			
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}
		
	}
	
	/**
	 * Recupera item de informações gerais
	 * 
	 * @return void
	 */
	@Get( PATH + "/info")
	@NoCache
	@Permissioned(value = AccessLevels.COMPANY_ADMIN, permissions = { ManagePolicyPermission.class })
	public void retrieveInfo(@NotNull @Valid  Long policyId){
		
		try {
			
			Policy policy = this.bs.exists(policyId, Policy.class);
			Item item= this.bs.listInfoByPolicy(policy);
			this.success(item);

		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
		
	}

	
	
	
	/**
	 * Salvar Novo Item
	 * 
	 * @return void
	 */
	@Post( PATH + "/new")
	@Consumes
	@NoCache
	public void saveItem(@NotNull @Valid Item item){
		try {	
			
			if(item.getPolicy()== null) {
				this.fail("Política não encontrada");
			}
			item.setId(null);
			this.bs.save(item);
			this.success(item);
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}

	
	/**
	 * Salvar Novo Subitem
	 * 
	 * @return void
	 */
	@Post( PATH + "/subnew")
	@Consumes
	@NoCache
	public void saveSubitem(@NotNull @Valid SubItem subitem){
		try {
			
			if(subitem.getItem() == null) {
				this.fail("Item não encontrada");
			}
			
			this.bs.save(subitem);
			this.success(subitem);
			
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}


	/**
	 * Salvar Novo FieldItem
	 * 
	 * @return void
	 */
	@Post( PATH + "/field")
	@Consumes
	@NoCache
	@Permissioned(value = AccessLevels.COMPANY_ADMIN, permissions = { ManagePolicyPermission.class })
	public void saveFieldItem(@NotNull @Valid FieldItem fieldItem){
		try {

			fieldItem.setId(null);
			this.bs.save(fieldItem);
			this.success(fieldItem);

		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}
	
	
	/**
	 * Salvar Novo FieldSubItem
	 * 
	 * @return void
	 */
	@Post( PATH + "/subfield")
	@Consumes
	@NoCache
	@Permissioned(value = AccessLevels.COMPANY_ADMIN, permissions = { ManagePolicyPermission.class })
	public void saveFieldSubItem(@NotNull @Valid FieldSubItem fieldSubItem){
		try {
			this.bs.save(fieldSubItem);
			this.success(fieldSubItem);

		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}
	
	
	/**
	 * Retorna itens.
	 * 
	 * @param Policy
	 *            Id do plano macro a ser retornado.
	 * @return <List> item
	 */
	@Get( PATH + "")
	@NoCache
	public void listItens(@NotNull Long policyId) {
		try {
			Policy policy = this.bs.exists(policyId, Policy.class);
			PaginatedList<Item> itens= this.bs.listItensByPolicy(policy);
			this.success(itens);
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}
	
	
	/**
	 * Retorna item.
	 * 
	 * @param id
	 *			Id do item a ser retornado.
	 * @return Item Retorna o item de acordo com o id passado.
	 */

	@Get( PATH + "/{id}")
	@NoCache
	@Permissioned
	public void retrieveItem(@NotNull Long id) {
		try {
			Item item = this.bs.exists(id, Item.class);
			if (item == null) {
				this.fail("O Item solicitado não foi encontrado.");
			} else {
				this.success(item);
			}
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}
	
	/**
	 * Retorna SubItem.
	 * 
	 * @param id
	 *			Id do SubItem a ser retornado.
	 * @return SubItem 
	 * 			Retorna o subitem de acordo com o id passado.
	 */

	@Get( PATH + "/subitem/{id}")
	@NoCache
	@Permissioned
	public void retrieveSubItem(@NotNull Long id) {
		try {
			SubItem subitem = this.bs.exists(id, SubItem.class);
			if (subitem == null) {
				this.fail("O SubItem solicitado não foi encontrado.");
			} else {
				this.success(subitem);
			}
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}
		
	
	/**
	 * Retorna campo.
	 * 
	 * @param id
	 *            Id do item.
	 */

	@Get( PATH + "/field/{id}")
	@NoCache
	@Permissioned
	public void retrieveField(@NotNull Long id) {
		
		try {
			
			Item item = this.bs.exists(id, Item.class);
			if (item == null) {
				this.fail("O item solicitado não foi encontrado.");
			} else {
				
				PaginatedList<FieldItem> fields = this.bs.listFieldsByItem(item);
				this.success(fields);
			}
			
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}
	
	
	/**
	 * Retorna campo.
	 * 
	 * @param id
	 *            Id do subitem.
	 */

	@Get( PATH + "/subfield/{id}")
	@NoCache
	@Permissioned
	public void retrieveSubField(@NotNull Long id) {
		
		try {
			
			SubItem subitem = this.bs.exists(id, SubItem.class);
			if (subitem == null) {
				this.fail("O subitem solicitado não foi encontrado.");
			} else {
				
				PaginatedList<FieldSubItem> fields = this.bs.listFieldsBySubItem(subitem);
				this.success(fields);
			}
			
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}



	

	/**
	 * Edita item.
	 * 
	 * @param item
	 *            Item a ser alterado com os novos campos.
	 */
	@Post( PATH + "/update")
	@Consumes
	@NoCache
	@Permissioned(value = AccessLevels.COMPANY_ADMIN, permissions = { ManagePolicyPermission.class })
	public void updateItem(@NotNull @Valid Item item) {
		try {
			Item existent = this.bs.exists(item.getId(), Item.class);
			if (GeneralUtils.isInvalid(existent)) {
				this.result.notFound();
				return;
			}

			if(existent.getPolicy()==null) {
				this.fail("Item sem política associada");
			}


			PaginatedList<FieldItem> fields = this.bs.listFieldsByItem(existent);
		
			for(int i=0; i<fields.getList().size();i++) {
				this.bs.delete(fields.getList().get(i));
			}

			
			for(int i=0; i<item.getFieldItem().size();i++) {
				FieldItem field =item.getFieldItem().get(i);
				
				field.setName(field.getValue());
				field.setItem(existent);
				this.bs.save(field);
			}
			
			existent.setDescription(item.getDescription());
			existent.setName(item.getName());
			this.bs.persist(existent);
			this.success(existent);
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Ocorreu um erro inesperado: " + ex.getMessage());
		}
	}
	
	
	/**
	 * Edita subitem.
	 * 
	 * @param subitem
	 *            Item a ser alterado com os novos campos.
	 */
	@Post( PATH + "/subitem/update")
	@Consumes
	@NoCache
	@Permissioned(value = AccessLevels.COMPANY_ADMIN, permissions = { ManagePolicyPermission.class })
	public void updateSubitem(@NotNull @Valid SubItem subitem) {
		try {
			SubItem existent = this.bs.exists(subitem.getId(), SubItem.class);
			if (GeneralUtils.isInvalid(existent)) {
				this.result.notFound();
				return;
			}

			if(existent.getItem()==null) {
				this.fail("SubItem sem Item associado");
			}


			PaginatedList<FieldSubItem> subfields = this.bs.listFieldsBySubItem(existent);
		
			for(int i=0; i<subfields.getList().size();i++) {
				this.bs.delete(subfields.getList().get(i));
			}

			for(int i=0; i<subitem.getFieldSubItem().size();i++) {
				FieldSubItem subfield =subitem.getFieldSubItem().get(i);
				
				subfield.setName(subfield.getValue());
				subfield.setSubitem(existent);
				this.bs.save(subfield);
			}
			
			existent.setDescription(subitem.getDescription());
			existent.setName(subitem.getName());
			this.bs.persist(existent);
			this.success(existent);
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Ocorreu um erro inesperado: " + ex.getMessage());
		}
	}
	
	
	/**
	 * Exclui item.
	 * 
	 * @param id
	 *            Id do item a ser excluído.
	 *            
	 */
	@Delete( PATH + "/{id}")
	@NoCache
	@Permissioned(value = AccessLevels.COMPANY_ADMIN, permissions = { ManagePolicyPermission.class })
	public void deleteItem(@NotNull Long id) {
		try {
			Item item = this.bs.exists(id, Item.class);
			if (GeneralUtils.isInvalid(item)) {
				this.result.notFound();
				return;
			}
			
			PaginatedList<FieldItem> fields = this.bs.listFieldsByItem(item);
			
			for(int i=0;i<fields.getList().size();i++) {
				this.bs.delete(fields.getList().get(i));
			}
			
			this.bs.delete(item);
			this.success();
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}
	}
	
	/**
	 * Exclui subitem.
	 * 
	 * @param id
	 *            Id do subitem a ser excluído.
	 *            
	 */
	@Delete( PATH + "/subitem/{id}")
	@NoCache
	@Permissioned(value = AccessLevels.COMPANY_ADMIN, permissions = { ManagePolicyPermission.class })
	public void deleteSubitem(@NotNull Long id) {
		try {
			SubItem subitem = this.bs.exists(id, SubItem.class);
			if (GeneralUtils.isInvalid(subitem)) {
				this.result.notFound();
				return;
			}
			
			PaginatedList<FieldSubItem> subfields = this.bs.listFieldsBySubItem(subitem);
			
			for(int i=0;i<subfields.getList().size();i++) {
				this.bs.delete(subfields.getList().get(i));
			}
			

			this.bs.delete(subitem);
			this.success();
		} catch (Throwable e) {
			LOGGER.error("Unexpected runtime error", e);
			this.fail("Ocorreu um erro inesperado: " + e.getMessage());
		}
	}
	
	
	/**
	 * Retorna subitens.
	 * 
	 * @param id
	 *            Id do item.
	 * @return Subitem Retorna os subitens de acordo com o id passado.
	 * 
	 */

	@Get( PATH + "/subitens/{id}")
	@NoCache
	@Permissioned
	public void retrieveSubitem(@NotNull Long id) {
		try {
			Item item = this.bs.exists(id, Item.class);
			if (item == null) {
				this.fail("A política solicitada não foi encontrado.");
			} else {
				PaginatedList<SubItem> subitens= this.bs.listSubItensByItem(item);
				this.success(subitens);
			}
		} catch (Throwable ex) {
			LOGGER.error("Unexpected runtime error", ex);
			this.fail("Erro inesperado: " + ex.getMessage());
		}
	}

}