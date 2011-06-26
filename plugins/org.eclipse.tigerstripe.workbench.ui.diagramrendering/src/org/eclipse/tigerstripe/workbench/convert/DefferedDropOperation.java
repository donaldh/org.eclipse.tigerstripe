package org.eclipse.tigerstripe.workbench.convert;

import java.util.Collections;
import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.AbstractOperation;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gmf.runtime.diagram.ui.commands.CommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.swt.dnd.DND;
import org.eclipse.tigerstripe.workbench.internal.core.util.Provider;
import org.eclipse.tigerstripe.workbench.internal.core.util.Tuple;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;

public class DefferedDropOperation extends AbstractOperation {

	private final EditPart editPart;
	private final List<Provider<IAbstractArtifact>> providers;
	private final List<Tuple<Point, Dimension>> sizes;
	private CommandProxy command;

	public DefferedDropOperation(EditPart editPart,
			List<Provider<IAbstractArtifact>> providers,
			List<Tuple<Point, Dimension>> sizes) {
		super(String.format("Drop artifacts"));
		this.editPart = editPart;
		this.providers = providers;
		this.sizes = sizes;
	}

	@Override
	public IStatus execute(IProgressMonitor monitor, IAdaptable info)
			throws ExecutionException {

		int size = providers.size();
		CompoundCommand compoundCommand = new CompoundCommand("Drop");
		for (int i = 0; i < size; ++i) {
			EditPolicy editPolicy = editPart
					.getEditPolicy(EditPolicyRoles.DRAG_DROP_ROLE);

			SizableDropRequest request = new SizableDropRequest();
			Tuple<Point, Dimension> ps = sizes.get(i);
			request.setLocation(ps.getFirst());
			request.setDimension(ps.getSecond());
			request.setObjects(Collections
					.singletonList(providers.get(i).get()));
			request.setAllowedDetail(DND.DROP_COPY);

			org.eclipse.gef.commands.Command cmd = editPolicy
					.getCommand(request);

			cmd.setLabel(getLabel());
			compoundCommand.add(cmd);
		}

		command = new CommandProxy(compoundCommand);
		return command.execute(monitor, info);
	}

	@Override
	public IStatus redo(IProgressMonitor monitor, IAdaptable info)
			throws ExecutionException {
		return command.redo(monitor, info);
	}

	@Override
	public IStatus undo(IProgressMonitor monitor, IAdaptable info)
			throws ExecutionException {
		return command.undo(monitor, info);
	}
}
